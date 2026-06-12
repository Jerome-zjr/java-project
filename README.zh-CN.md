# 3D 地牢探险 (3D Roguelike Dungeon)

[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](#license)
[![No Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen.svg)]()

**[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)**

一个纯 Java 实现的第一人称 3D 地牢探险游戏，采用 **Java 17** 和 **Swing/AWT** 构建 — 无外部依赖。具有光线投射 3D 渲染、过程化地牢生成和经典 Roguelike 机制，提供独立、跨平台的游戏体验。

## 目录

- [截图](#截图)
- [创新亮点](#创新亮点)
- [功能特性](#功能特性)
- [游戏机制](#游戏机制)
- [技术栈](#技术栈)
- [架构和设计模式](#架构和设计模式)
- [操作说明](#操作说明)
- [编译和运行](#编译和运行)
- [项目结构](#项目结构)
- [扩展游戏](#扩展游戏)
- [开发指南](#开发指南)
- [故障排除](#故障排除)
- [开发路线图](#开发路线图)
- [许可证](#许可证)
- [贡献者](#贡献者)

## 截图

| 菜单 | 游戏内 (3D 视图) |
|------|-------------------|
| ![menu](https://github.com/user-attachments/assets/e3247e4b-844e-48b6-a09f-f2cd708d50f7) | ![ingame](https://github.com/user-attachments/assets/e7a35325-dbee-4685-a7d2-cae88a728a89) |

## 创新亮点

- **零依赖 3D 引擎**：纯 Java 实现，仅使用 Swing/AWT，无需外部库
- **DDA 光线投射算法**：高效的距离基础渲染，灵感来自 Wolfenstein 3D
- **过程化生成**：智能房间走廊布置，保证房间连通性
- **可扩展敌人系统**：支持多种敌人类型的可扩展架构，各具特色行为
- **跨平台兼容**：只要安装 Java 17+，无需编译即可在任何平台运行

## 功能特性

### 3D 渲染
- **3D 光线投射渲染器** — 经典 DDA 算法（Wolfenstein / Lode 风格）
  - 基于距离的墙体阴影和颜色渐变
  - 敌人和物品的公告牌精灵投影
  - 独立 Z 缓冲用于正确的精灵遮挡
  - 优化的光线追踪与早期终止

### 世界生成和进度
- **过程化地牢生成** — 智能房间布置，保证 L 形走廊连通
- **18 个主题楼层** — 每层独特的调色板和粒子效果，视觉上显示进度
- **持久进度** — 跨遭遇的楼层计数器和分数追踪

### 游戏系统
- **Roguelike 机制** — 永久死亡、基于分数的持久化、楼层进度、失败后果
- **三种敌人类型** — 骷髅兵、僵尸、恶魔（各具独特属性：生命值、攻击、防御、速度）
- **物品系统** — 生命药瓶、力量药瓶、护甲碎片，接近时自动收集
- **战斗机制** — ±20% 伤害浮动、近战攻击范围、敌人 AI 包括追击和撤退逻辑

### 用户界面
- **HUD 系统** — 生命值条（颜色编码）、楼层/分数显示、可滚动战斗消息日志
- **小地图** — 实时地牢覆盖图，显示玩家位置和方向指示器
- **准心** — 中心瞄准准心
- **菜单** — 专业标题屏幕和操作说明、游戏结束屏幕显示最终分数

## 游戏机制

### 战斗系统
- **伤害公式**：`最终伤害 = 基础伤害 × 攻击者防御加成 × 浮动(±20%)`
- **敌人 AI**：敌人在渲染距离内检测玩家，视线范围内追击，低血时撤退
- **玩家攻击**：近战挥砍，方向基于玩家视角
- **防御**：护甲碎片叠加以减少伤害

### 进度系统
- **难度递增**：每层增加敌人属性和生成率
- **物品分布**：物品随机出现在楼层，后期楼层生成率更高
- **分数计算**：击杀敌人和楼层进度时获得分数

### 地图生成算法
1. 在随机位置放置初始房间，最小距离间隔
2. 使用 L 形走廊连接房间
3. 在最后房间放置楼梯
4. 使用图遍历验证连通性

## 技术栈

- **语言**：Java 17（现代语言特性：records、sealed classes、pattern matching）
- **图形**：Swing (JPanel, Graphics2D) + AWT (MouseListener, KeyListener)
- **构建系统**：Maven 3.x（支持纯 `javac` 备选方案）
- **渲染**：纯算法实现（无外部图形库）
- **依赖**：**零** — 所有功能都从零实现
- **目标运行时**：JVM 17+

这种零依赖方法展示了软件工程卓越和对低级渲染概念的深刻理解，是理想的教育资源和作品集样本。

## 架构和设计模式

### 使用的设计模式

1. **状态机模式**（`GameState` 枚举）
   - 管理游戏模式：菜单 → 游戏中 → 游戏结束
   - 状态转移在 `Game.update()` 中处理

2. **实体-组件模式**
   - `Entity` 基类包含通用属性（生命值、位置、角度）
   - 专门的子类：`Player`、`Enemy`
   - 通过 `EnemyType` 枚举的特定类型行为

3. **策略模式**（`EnemyType`）
   - 不同敌人类型的 AI 行为分离
   - 配置与行为解耦

4. **工厂模式**（`MapGenerator`）
   - 游戏世界的过程化生成
   - 封装复杂的房间/走廊逻辑

5. **类观察者的输入处理**
   - `InputHandler` 维护按键状态而无紧耦合
   - `Game` 每帧轮询输入状态

### 分层架构

- **表现层**：`Game` (JPanel)、`Menu`、`HUD`、`RaycastRenderer`
- **游戏逻辑层**：`Entity`、`Player`、`Enemy`、`CombatSystem`
- **数据层**：`DungeonMap`、`Tile`、`Item`、`ItemType`
- **工具库**：`ColorPalette`、`MathUtils`

## 操作说明

| 按键 | 操作 |
|-----|--------|
| `W` / `↑` | 向前移动 |
| `S` / `↓` | 向后移动 |
| `A` / `←` | 左转 |
| `D` / `→` | 右转 |
| `Q` | 左走位 |
| `E` | 右走位 |
| `SPACE` | 攻击（近战挥砍） |
| `F` | 下楼（站在 `▼` 上时） |
| `ENTER` | 开始游戏 / 确认 |
| `ESC` | 退出 |

## 编译和运行

### 需求
- **Java 17+**（从 [oracle.com](https://www.oracle.com/java/) 下载或使用 OpenJDK）
- **Maven 3.x**（可选 — 也可使用纯 `javac`）

### 使用 Maven 快速开始

```bash
# 克隆并进入项目目录
git clone https://github.com/Jerome-zjr/java-project.git
cd java-project

# 编译
mvn clean package -DskipTests

# 运行
java -jar target/roguelike3d.jar
```

### 使用纯 `javac` 编译

```bash
mkdir -p build/classes
find src/main/java -name "*.java" > sources.txt
javac -d build/classes @sources.txt
jar cfe build/roguelike3d.jar com.roguelike3d.Main -C build/classes .
java -jar build/roguelike3d.jar
```

### 跨平台说明
- **Windows**：确保 Java 在 PATH 中；命令中使用正斜杠
- **macOS**：可能需要 XQuartz 来完全支持 Swing
- **Linux**：需要 X11；如有问题，测试 `xhost +local:`

## 项目结构

```
src/main/java/com/roguelike3d/
├── Main.java                   程序入口
├── Game.java                   游戏循环、状态机、JPanel
├── GameState.java              枚举：菜单 / 游戏中 / 游戏结束
├── input/
│   └── InputHandler.java       键盘状态（按住 + 刚按下）
├── map/
│   ├── Tile.java               地板 / 墙壁 / 下楼梯
│   ├── DungeonMap.java         网格存储
│   └── MapGenerator.java       过程化房间+走廊生成器
├── entity/
│   ├── Entity.java             基类（生命值、攻击、防御）
│   ├── Player.java             玩家（视角、楼层、分数、背包）
│   ├── Enemy.java              敌人 AI（追击 + 攻击计时器）
│   └── EnemyType.java          骷髅兵 / 僵尸 / 恶魔
├── combat/
│   └── CombatSystem.java       伤害公式，±20% 浮动
├── item/
│   ├── Item.java               地面物品，接近时自动收集
│   └── ItemType.java           生命药 / 力量药 / 护甲碎片
├── renderer/
│   └── RaycastRenderer.java    DDA 光线投射墙壁 + 精灵公告牌
└── ui/
    ├── HUD.java                生命条、小地图、消息、准心
    └── Menu.java               标题屏幕
```

## 扩展游戏

### 添加新敌人类型

**文件**：`src/main/java/com/roguelike3d/entity/EnemyType.java`

添加新常量及其属性：
```java
PHANTOM(3, 8, 1, 2.0f, 0xAA00AA) // 名字、生命值、攻击、防御、速度、颜色
```

属性说明：
- `hp`：生命值（基础值）
- `attack`：每次挥砍的攻击伤害
- `defense`：防御倍数（伤害减免）
- `speed`：相对基础的移动速度倍数
- `color`：RGB 十六进制颜色用于渲染

**文件**：`src/main/java/com/roguelike3d/entity/Enemy.java`

更新构造函数以处理新类型属性。行为已通过 `Enemy.update()` 中的状态机通用化。

### 添加新物品类型

**文件**：`src/main/java/com/roguelike3d/item/ItemType.java`

添加新常量：
```java
MANA_SHARD(0x00FFFF, 15) // 颜色、效果值
```

**文件**：`src/main/java/com/roguelike3d/item/Item.java`

更新 `collect()` 方法，添加新的 case：
```java
case MANA_SHARD -> {
    // 应用法力恢复或自定义效果
    player.setMana(Math.min(100, player.getMana() + 30));
}
```

### 调整游戏难度

**文件**：`src/main/java/com/roguelike3d/map/MapGenerator.java`

调整难度常量：
```java
private static final int ROOM_COUNT = 8;        // 房间多 = 游戏时间长
private static final int MIN_ROOM_SIZE = 5;     // 房间大 = 导航容易
private static final int FLOOR_COUNT = 18;      // 总楼层数
```

**文件**：`src/main/java/com/roguelike3d/entity/Enemy.java`

调整每层属性增长：
```java
float floorMultiplier = 1.0f + (floor * 0.1f);  // 每层增加 10%
```

### 添加墙纹理

**文件**：`src/main/java/com/roguelike3d/renderer/RaycastRenderer.java`

替换当前的 `shade()` 方法（使用简单颜色渐变）为纹理映射：

1. 在构造函数中加载纹理图集为 `BufferedImage`
2. 在光线命中计算中，确定纹理坐标（u, v）
3. 采样纹理像素并应用基于距离的变暗
4. 渲染纹理像素而不是平面颜色

**伪代码**：
```java
int textureColor = textureAtlas.getRGB(u * textureWidth, v * textureHeight);
int shadedColor = applyDistanceDarkening(textureColor, distance);
pixel.setColor(shadedColor);
```

### 添加音效

**文件**：`src/main/java/com/roguelike3d/combat/CombatSystem.java`

使用 Java 的 `javax.sound.sampled` API：
```java
import javax.sound.sampled.*;

public void playSound(String soundFile) throws Exception {
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundFile));
    Clip clip = AudioSystem.getClip();
    clip.open(audioStream);
    clip.start();
}
```

在 `attack()` 方法中调用：
```java
playSound("sounds/slash.wav");
```

### 持久高分追踪

**文件**：`src/main/java/com/roguelike3d/Game.java`

在 `renderGameOver()` 中添加文件 I/O：
```java
PrintWriter writer = new PrintWriter("highscores.txt", "UTF-8");
writer.println("Player: " + player.getName() + " | Score: " + player.getScore() + 
               " | Floor: " + player.getFloor());
writer.close();
```

从 `Menu.render()` 在标题屏幕上加载并显示。

### 自定义地牢主题

在 `ColorPalette.java` 中添加可配置调色板：
```java
public static final int[] LAVA_THEME = {0xFF4400, 0xFF6600, 0xFF8800, ...};
public static final int[] ICE_THEME = {0x00AAFF, 0x00CCFF, 0x00EEFF, ...};
```

根据当前楼层传递主题给 `RaycastRenderer`。

## 开发指南

### 代码风格
- 使用清晰、描述性的变量名（`rayDistance` 而不是 `rd`）
- 保持方法专注（单一职责）
- 对魔数使用常量（见 `MapGenerator.ROOM_COUNT`）
- 记录复杂算法的文档（尤其是光线投射逻辑）

### 性能考虑
- **帧率**：目标 60 FPS；使用 `System.nanoTime()` 测试
- **光线投射**：每帧约 240 条光线（约 60 条/毫秒）；使用早期终止
- **内存**：典型 ~50MB；避免游戏循环中的对象分配
- **优化建议**：
  - 在 `ColorPalette` 中缓存颜色计算
  - 尽可能使用整数算术而非浮点数
  - 使用 JProfiler 或内置 `-prof` 标志进行分析

### 测试方法
1. **手动测试**：玩多局，改变楼层计数
2. **边界情况**：测试地图边界、碰撞角落情况
3. **性能**：在 1920x1080 分辨率下监控帧率

### 添加测试

创建 `src/test/java/` 并添加单元测试：
```java
public class MapGeneratorTest {
    @Test
    public void testMapConnectivity() {
        DungeonMap map = MapGenerator.generate(10);
        assertTrue(isConnected(map));
    }
}
```

运行方式：
```bash
mvn test
```

## 故障排除

### 游戏无法启动
- **错误**："Main-Class not found"
  - **解决方案**：确保 `pom.xml` 有正确的 `<mainClass>` 条目
  
- **错误**："NoSuchMethodError"
  - **解决方案**：检查 Java 版本是否为 17+：`java -version`

### 渲染问题
- **图形失真**：尝试调整窗口大小或重启
- **启动时黑屏**：检查显卡驱动；尝试 `java -Dswing.disableBufferPerComponent=true -jar target/roguelike3d.jar`
- **闪烁/撕裂**：增加缓冲区大小或禁用 vsync（如可用）

### 性能问题
- **FPS 低**：降低屏幕分辨率或在 `Game.java` 中增加帧跳过间隔
- **内存泄漏**：检查 `Item` 对象在收集后是否被正确垃圾回收

### 编译失败
- **找不到 Maven**：确保已安装 Maven：`mvn -version`
- **源文件未找到**：验证所有 Java 文件在 `src/main/java/` 中

## 开发路线图

### 版本 1.1（计划中）
- [ ] 可配置难度等级（简单/普通/困难）
- [ ] 排行榜系统，支持持久高分
- [ ] 额外敌人类型（幽灵、石头人）
- [ ] 更多物品类型（速度提升、临时护盾）
- [ ] 武器多样性（剑、弓、魔法杖）

### 版本 1.2（计划中）
- [ ] 多人支持（本地合作，分屏模式）
- [ ] 里程碑楼层的 Boss 战斗
- [ ] 冷却时间的法术/魔法系统
- [ ] 墙纹理和高级渲染效果
- [ ] 音效和环境音乐
- [ ] 通过插件架构的 Mod 支持

### 版本 2.0（未来展望）
- [ ] 网络多人模式（与朋友在线地牢爬行）
- [ ] 高级 AI 和寻路（A* 算法）
- [ ] 故事活动和 NPC 互动
- [ ] 完整的 6-DOF 运动（向上/向下看）
- [ ] 动态阴影的光照系统
- [ ] 跨平台编译为本地二进制（GraalVM Native Image）

## 许可证

本项目采用 **MIT 许可证** 分发。详见 [LICENSE](LICENSE) 文件。

MIT 许可证允许在商业项目中自由使用、修改和分发，唯一要求是注明出处。

## 贡献者

- **Jerome-zjr** — 原始作者和维护者

## 致谢

- 灵感来自经典 Roguelike 游戏（NetHack、Dungeon Crawl）
- 光线投射算法基于 Wolfenstein 3D 技术
- 社区反馈和问题报告

---

**有问题或想贡献？** [提交 issue](https://github.com/Jerome-zjr/java-project/issues) 或提交 pull request！
