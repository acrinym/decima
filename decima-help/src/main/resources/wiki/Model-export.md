### Supported types

Decima Workshop can export the following types:

|Death Stranding|Horizon Zero Dawn|
|---|---|
|<ul><li>`ArtPartsDataResource`<ul></ul></li><li>`MeshResourceBase`<ul><li>`AnimatedStaticMeshResource`</li><li>`BlendedMeshResource`</li><li>`HairResource`</li><li>`LodMeshResource`</li><li>`MultiMeshResource`</li><li>`RegularSkinnedMeshResourceBase`</li><li>`RegularSkinnedMeshResource`</li><li>`SkinnedMeshResource`</li><li>`StaticMeshResource`</li></ul></li><li>`ObjectCollection`<ul></ul></li><li>`StaticMeshResource`<ul><li>`AnimatedStaticMeshResource`</li></ul></li></ul>|<ul><li>`ControlledEntityResource`<ul><li>`CaptureAndHoldAreaResource`</li><li>`ExplosiveLocationResource`</li><li>`HumanoidResource`</li><li>`InteractiveEntityResource`</li><li>`MountableEntityResource`</li><li>`PlayAnimationObjectResource`</li><li>`SoldierResource`</li><li>`SpawnAreaResource`</li><li>`SwitchResource`</li><li>`TurretResource`</li><li>`ValveResource`</li></ul></li><li>`MeshResourceBase`<ul><li>`BlendedMeshResource`</li><li>`HairResource`</li><li>`InstancedMeshResource`</li><li>`LodMeshResource`</li><li>`MultiMeshResource`</li><li>`RegularSkinnedMeshResourceBase`</li><li>`RegularSkinnedMeshResource`</li><li>`SkinnedMeshResource`</li><li>`StaticMeshResource`</li></ul></li><li>`ObjectCollection`<ul></ul></li><li>`SkinnedModelResource`<ul></ul></li><li>`StaticMeshResource`<ul></ul></li><li>`StreamingTileResource`<ul></ul></li><li>`Terrain`<ul></ul></li><li>`TileBasedStreamingStrategyResource`<ul></ul></li></ul>|

<!--
Arrays.stream(ModelViewer.class.getDeclaredAnnotation(ValueViewerRegistration.class).value())
    .filter(type -> IOUtils.indexOf(type.game(), GameType.DS) >= 0)
    .map(Type::name)
    .sorted()
    .map(name -> {
        final String types = typeRegistry.hashByType.keySet().stream()
            .filter(type -> type instanceof RTTIClass cls && !name.equals(cls.getFullTypeName()) && cls.isInstanceOf(name))
            .map(x -> "<li>`%s`</li>".formatted(x.getFullTypeName()))
            .sorted()
            .collect(Collectors.joining());
        return "<li>`%s`<ul>%s</ul></li>".formatted(name, types);
    })
    .collect(Collectors.joining("", "<ul>", "</ul>"));
-->

For data exchange, we use an in-house format called `DMF` (Decima Model Format) that has a counterpart addon for [Blender](https://www.blender.org/), a free 3D computer graphics software tool.

### Installing the addon

1. Download the latest addon version from [here](https://github.com/REDxEYE/decima-dmf), or [click here](https://github.com/REDxEYE/decima-dmf/archive/refs/heads/master.zip) for a direct download link
1. Open Blender, go to <kbd>Edit</kbd> &rArr; <kbd>Preferences</kbd> &rArr; <kbd>Add-ons</kbd> &rArr; <kbd>Install</kbd> and choose the zip file you've downloaded 

### Exporting models

1. In Decima Workshop, choose an entry of type among [supported types](#supported-types). A panel on the right will pop up
1. Choose appropriate options and click <kbd>Export</kbd> and save the file somewhere
1. Open Blender, go to <kbd>File</kbd> &rArr; <kbd>Import</kbd> &rArr; <kbd>Decima Model (.dmf)</kbd> and choose the file you've exported
