# fidworkblocks

`flavor_immersed_daily_neoforge/fidworkblocks` 是本工作区的附属构建，`modid` 为 `fidworkblock`。

这里是本模组的核心工作方块模块：蒸笼、炒锅、高压锅、砧板、炸锅、变温烤箱、压榨机、坛子、茶壶、汤锅、研钵、绞肉机。
方块本体、方块实体逻辑、`fidworkblock:cooking` 配方类型与全部配方数据都在这里。

| 路径 | 内容 |
| --- | --- |
| `src/main/java/com/fidtest/block/entity/` | 各工作方块的加工逻辑 |
| `src/main/resources/data/fidworkblock/recipe/<appliance>/` | 各工作方块的配方数据 |
| `src/main/resources/data/<命名空间>/choppingboard_mob/` | 砧板小型生物屠宰定义 |
| `src/main/resources/assets/fidworkblock/` | 贴图、模型、语言文件 |

写配方直接照抄对应目录里的现成 JSON，不用另看文档。

This directory is the add-on build of the workspace; `modid` is `fidworkblock`.

It holds the core work blocks of the mod: steamer, wok, pressure cooker, chopping board, deep fryer,
temperature-controlled oven, press, jar, teapot, soup pot, mortar and meat grinder — including the
block logic, the `fidworkblock:cooking` recipe type and all recipe data.

| Path | Contents |
| --- | --- |
| `src/main/java/com/fidtest/block/entity/` | Processing logic of each work block |
| `src/main/resources/data/fidworkblock/recipe/<appliance>/` | Recipe data of each work block |
| `src/main/resources/data/<namespace>/choppingboard_mob/` | Chopping board butchery definitions |
| `src/main/resources/assets/fidworkblock/` | Textures, models, language files |

Copy an existing JSON from the matching directory instead of reading a separate guide.
