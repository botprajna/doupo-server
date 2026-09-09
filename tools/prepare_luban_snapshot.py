"""Prepare a read-only-source Luban snapshot from the client's local mirror.

The client AssemblyEncrypt -> OdinXOREncrypt uses a repeating 256-byte key
(libil2cpp RVA 0x2B02444 -> 0x2ABF030 -> 0x2ABEE48; cctor 0x2ABF08C).
Recover that key from a known ciphertext/plaintext pair, verify the entire old
DLL, then validate the new PE/CLR metadata before publishing a decoded copy.
No network access, runtime file replacement, or authentication changes.
"""
import argparse
import hashlib
import json
import sys
import zipfile
from pathlib import Path


def digest(data):
    return hashlib.sha256(data).hexdigest()


def write_once(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        if path.read_bytes() != data:
            raise ValueError("Refusing to replace a different snapshot file: " + str(path))
    else:
        with path.open("xb") as handle:
            handle.write(data)


def decode_dll(data, key):
    result = bytes(value ^ key[index % len(key)] for index, value in enumerate(data))
    pe = int.from_bytes(result[60:64], "little")
    if result[:2] != b"MZ" or result[pe:pe + 4] != b"PE\0\0" or b"BSJB" not in result:
        raise ValueError("Decoded data is not a managed PE")
    return result


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mirror", required=True, help="Local directory ending in Ljxs")
    parser.add_argument("--snapshot", required=True)
    parser.add_argument("--config-out", required=True)
    parser.add_argument("--old-apk", required=True)
    parser.add_argument("--old-dll", required=True)
    parser.add_argument("--catalog-tools", required=True)
    args = parser.parse_args()
    sys.path.insert(0, args.catalog_tools)
    from catalog_parser import parse_catalog
    from download_configs import collect_entries

    mirror, snapshot, output = map(Path, (args.mirror, args.snapshot, args.config_out))
    catalog = mirror / "Catalog/catalog_Ljxs.bin"
    patch = json.loads((mirror / "PatchSetting/patchSetting.json").read_text(encoding="utf-8"))
    assemblies = json.loads((mirror / "Assemblies/hotUpdateAssemblyInfo.json").read_text(encoding="utf-8"))
    if (patch["m_MajorVersion"], patch["m_MinorVersion"], patch["m_ResourceVersion"]) != (6, 9, 263):
        raise ValueError("Expected the pinned local 6.9 resource 263 snapshot")
    for relative in ["Catalog/catalog_Ljxs.bin", "Catalog/catalog_Ljxs.hash",
                     "PatchSetting/patchSetting.json", "Assemblies/hotUpdateAssemblyInfo.json"]:
        write_once(snapshot / relative, (mirror / relative).read_bytes())

    plain = Path(args.old_dll).read_bytes()
    with zipfile.ZipFile(args.old_apk) as apk:
        encrypted = apk.read("assets/Assemblies/HotUpdateAssemblies/SGEngine.Configs.Runtime.dll.bytes")
    key = bytes(a ^ b for a, b in zip(encrypted[:256], plain[:256]))
    if len(key) != 256 or decode_dll(encrypted, key) != plain:
        raise ValueError("Known-pair verification failed; do not guess a decoder")
    dlls = []
    for name in ["SGEngine.Configs.Runtime.dll", "SGEngine.Gameplay.Runtime.dll"]:
        i = assemblies["assemblyFileNames"].index(name + ".bytes")
        source = mirror / "Assemblies/HotUpdateAssemblies" / assemblies["assemblyFileNamesWithHash"][i]
        data = source.read_bytes()
        if len(data) != assemblies["assemblySizes"][i]:
            raise ValueError("DLL length disagrees with assembly manifest: " + name)
        decoded = decode_dll(data, key)
        write_once(snapshot / "decoded" / name, decoded)
        dlls.append({"name": name, "source": str(source), "encryptedSha256": digest(data),
                     "decodedSha256": digest(decoded), "bytes": len(data)})

    entries = collect_entries(parse_catalog(str(catalog)))
    rows = []
    for bundle, locations in sorted(entries.items()):
        source = mirror / "ServerData" / bundle[-9:-7] / bundle
        data = source.read_bytes()  # Missing data stops the export instead of mixing old tables.
        if not data:
            raise ValueError("Empty config bundle: " + str(source))
        for name, internal_id in locations:
            destination = (output / (name + ".bytes")).resolve()
            if not destination.is_relative_to(output.resolve()):
                raise ValueError("Unsafe catalog key: " + name)
            write_once(destination, data)
            rows.append({"key": name, "internalId": internal_id, "bundle": bundle,
                         "bytes": len(data), "sha256": digest(data)})
    manifest = {"version": "6.9.263", "resourceVersion": 263,
                "patchAssemblyVersion": patch["m_AssemblyVersion"],
                "assemblyManifestVersion": assemblies["version"],
                "catalogSha256": digest(catalog.read_bytes()), "dlls": dlls,
                "decoderKnownPlaintextSha256": digest(plain), "decoderKeySha256": digest(key),
                "rawDataCount": len(rows), "rawData": rows}
    write_once(snapshot / "manifest.json", json.dumps(manifest, ensure_ascii=False, indent=2).encode("utf-8"))
    print(json.dumps({"rawDataCount": len(rows), "dlls": dlls,
                      "manifest": str(snapshot / "manifest.json")}, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
