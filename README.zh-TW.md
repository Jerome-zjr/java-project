# 3D 地牢探險 (3D Roguelike Dungeon)

[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](#license)
[![No Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen.svg)]()

**[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)**

一個純 Java 實現的第一人稱 3D 地牢探險遊戲，採用 **Java 17** 和 **Swing/AWT** 構建 — 無外部依賴。具有光線投射 3D 渲染、程序化地牢生成和經典 Roguelike 機制，提供獨立、跨平台的遊戲體驗。

## 目錄

- [截圖](#截圖)
- [創新亮點](#創新亮點)
- [功能特性](#功能特性)
- [遊戲機制](#遊戲機制)
- [技術棧](#技術棧)
- [架構和設計模式](#架構和設計模式)
- [操作說明](#操作說明)
- [編譯和運行](#編譯和運行)
- [項目結構](#項目結構)
- [擴展遊戲](#擴展遊戲)
- [開發指南](#開發指南)
- [故障排除](#故障排除)
- [開發路線圖](#開發路線圖)
- [許可證](#許可證)
- [貢獻者](#貢獻者)

## 截圖

| 菜單 | 遊戲內 (3D 視圖) |
|------|-------------------|
| ![menu](https://github.com/user-attachments/assets/e3247e4b-844e-48b6-a09f-f2cd708d50f7) | ![ingame](https://github.com/user-attachments/assets/e7a35325-dbee-4685-a7d2-cae88a728a89) |

## 創新亮點

- **零依賴 3D 引擎**：純 Java 實現，僅使用 Swing/AWT，無需外部庫
- **DDA 光線投射算法**：高效的距離基礎渲染，靈感來自 Wolfenstein 3D
- **程序化生成**：智能房間走廊佈置，保證房間連通性
- **可擴展敵人系統**：支持多種敵人類型的可擴展架構，各具特色行為
- **跨平台兼容**：只要安裝 Java 17+，無需編譯即可在任何平台運行

## 功能特性

### 3D 渲染
- **3D 光線投射渲染器** — 經典 DDA 算法（Wolfenstein / Lode 風格）
  - 基於距離的牆體陰影和顏色漸變
  - 敵人和物品的公告牌精靈投影
  - 獨立 Z 緩衝用於正確的精靈遮擋
  - 優化的光線追踪與早期終止

### 世界生成和進度
- **程序化地牢生成** — 智能房間佈置，保證 L 形走廊連通
- **18 個主題樓層** — 每層獨特的調色板和粒子效果，視覺上顯示進度
- **持久進度** — 跨遭遇的樓層計數器和分數追蹤

### 遊戲系統
- **Roguelike 機制** — 永久死亡、基於分數的持久化、樓層進度、失敗後果
- **三種敵人類型** — 骷髏兵、殭屍、惡魔（各具獨特屬性：生命值、攻擊、防禦、速度）
- **物品系統** — 生命藥瓶、力量藥瓶、護甲碎片，接近時自動收集
- **戰鬥機制** — ±20% 傷害浮動、近戰攻擊範圍、敵人 AI 包括追擊和撤退邏輯

### 用戶界面
- **HUD 系統** — 生命值條（顏色編碼）、樓層/分數顯示、可滾動戰鬥消息日誌
- **小地圖** — 實時地牢覆蓋圖，顯示玩家位置和方向指示器
- **準心** — 中心瞄準準心
- **菜單** — 專業標題屏幕和操作說明、遊戲結束屏幕顯示最終分數

## 遊戲機制

### 戰鬥系統
- **傷害公式**：`最終傷害 = 基礎傷害 × 攻擊者防禦加成 × 浮動(±20%)`
- **敵人 AI**：敵人在渲染距離內檢測玩家，視線範圍內追擊，低血時撤退
- **玩家攻擊**：近戰揮砍，方向基於玩家視角
- **防禦**：護甲碎片疊加以減少傷害

### 進度系統
- **難度遞增**：每層增加敵人屬性和生成率
- **物品分佈**：物品隨機出現在樓層，後期樓層生成率更高
- **分數計算**：擊殺敵人和樓層進度時獲得分數

### 地圖生成算法
1. 在隨機位置放置初始房間，最小距離間隔
2. 使用 L 形走廊連接房間
3. 在最後房間放置樓梯
4. 使用圖遍歷驗證連通性

## 技術棧

- **語言**：Java 17（現代語言特性：records、sealed classes、pattern matching）
- **圖形**：Swing (JPanel, Graphics2D) + AWT (MouseListener, KeyListener)
- **構建系統**：Maven 3.x（支持純 `javac` 備選方案）
- **渲染**：純算法實現（無外部圖形庫）
- **依賴**：**零** — 所有功能都從零實現
- **目標運行時**：JVM 17+

這種零依賴方法展示了軟件工程卓越和對低級渲染概念的深刻理解，是理想的教育資源和作品集樣本。

## 架構和設計模式

### 使用的設計模式

1. **狀態機模式**（`GameState` 枚舉）
   - 管理遊戲模式：菜單 → 遊戲中 → 遊戲結束
   - 狀態轉移在 `Game.update()` 中處理

2. **實體-組件模式**
   - `Entity` 基類包含通用屬性（生命值、位置、角度）
   - 專門的子類：`Player`、`Enemy`
   - 通過 `EnemyType` 枚舉的特定類型行為

3. **策略模式**（`EnemyType`）
   - 不同敵人類型的 AI 行為分離
   - 配置與行為解耦

4. **工廠模式**（`MapGenerator`）
   - 遊戲世界的程序化生成
   - 封裝複雜的房間/走廊邏輯

5. **類觀察者的輸入處理**
   - `InputHandler` 維護按鍵狀態而無緊耦合
   - `Game` 每幀輪詢輸入狀態

### 分層架構

- **表現層**：`Game` (JPanel)、`Menu`、`HUD`、`RaycastRenderer`
- **遊戲邏輯層**：`Entity`、`Player`、`Enemy`、`CombatSystem`
- **數據層**：`DungeonMap`、`Tile`、`Item`、`ItemType`
- **工具庫**：`ColorPalette`、`MathUtils`

## 操作說明

| 按鍵 | 操作 |
|-----|--------|
| `W` / `↑` | 向前移動 |
| `S` / `↓` | 向後移動 |
| `A` / `←` | 左轉 |
| `D` / `→` | 右轉 |
| `Q` | 左走位 |
| `E` | 右走位 |
| `SPACE` | 攻擊（近戰揮砍） |
| `F` | 下樓（站在 `▼` 上時） |
| `ENTER` | 開始遊戲 / 確認 |
| `ESC` | 退出 |

## 編譯和運行

### 需求
- **Java 17+**（從 [oracle.com](https://www.oracle.com/java/) 下載或使用 OpenJDK）
- **Maven 3.x**（可選 — 也可使用純 `javac`）

### 使用 Maven 快速開始

```bash
# 克隆並進入項目目錄
git clone https://github.com/Jerome-zjr/java-project.git
cd java-project

# 編譯
mvn clean package -DskipTests

# 運行
java -jar target/roguelike3d.jar
```

### 使用純 `javac` 編譯

```bash
mkdir -p build/classes
find src/main/java -name "*.java" > sources.txt
javac -d build/classes @sources.txt
jar cfe build/roguelike3d.jar com.roguelike3d.Main -C build/classes .
java -jar build/roguelike3d.jar
```

### 跨平台說明
- **Windows**：確保 Java 在 PATH 中；命令中使用正斜杠
- **macOS**：可能需要 XQuartz 來完全支持 Swing
- **Linux**：需要 X11；如有問題，測試 `xhost +local:`

## 項目結構

```
src/main/java/com/roguelike3d/
├── Main.java                   程序入口
├── Game.java                   遊戲迴圈、狀態機、JPanel
├── GameState.java              枚舉：菜單 / 遊戲中 / 遊戲結束
├── input/
│   └── InputHandler.java       鍵盤狀態（按住 + 剛按下）
├── map/
│   ├── Tile.java               地板 / 牆壁 / 下樓梯
│   ├── DungeonMap.java         網格存儲
│   └── MapGenerator.java       程序化房間+走廊生成器
├── entity/
│   ├── Entity.java             基類（生命值、攻擊、防禦）
│   ├── Player.java             玩家（視角、樓層、分數、背包）
│   ├── Enemy.java              敵人 AI（追擊 + 攻擊計時器）
│   └── EnemyType.java          骷髏兵 / 殭屍 / 惡魔
├── combat/
│   └── CombatSystem.java       傷害公式，±20% 浮動
├── item/
│   ├── Item.java               地面物品，接近時自動收集
│   └── ItemType.java           生命藥 / 力量藥 / 護甲碎片
├── renderer/
│   └── RaycastRenderer.java    DDA 光線投射牆壁 + 精靈公告牌
└── ui/
    ├── HUD.java                生命條、小地圖、消息、準心
    └── Menu.java               標題屏幕
```

## 擴展遊戲

### 添加新敵人類型

**文件**：`src/main/java/com/roguelike3d/entity/EnemyType.java`

添加新常量及其屬性：
```java
PHANTOM(3, 8, 1, 2.0f, 0xAA00AA) // 名字、生命值、攻擊、防禦、速度、顏色
```

屬性說明：
- `hp`：生命值（基礎值）
- `attack`：每次揮砍的攻擊傷害
- `defense`：防禦倍數（傷害減免）
- `speed`：相對基礎的移動速度倍數
- `color`：RGB 十六進制顏色用於渲染

**文件**：`src/main/java/com/roguelike3d/entity/Enemy.java`

更新構造函數以處理新類型屬性。行為已通過 `Enemy.update()` 中的狀態機通用化。

### 添加新物品類型

**文件**：`src/main/java/com/roguelike3d/item/ItemType.java`

添加新常量：
```java
MANA_SHARD(0x00FFFF, 15) // 顏色、效果值
```

**文件**：`src/main/java/com/roguelike3d/item/Item.java`

更新 `collect()` 方法，添加新的 case：
```java
case MANA_SHARD -> {
    // 應用法力恢復或自定義效果
    player.setMana(Math.min(100, player.getMana() + 30));
}
```

### 調整遊戲難度

**文件**：`src/main/java/com/roguelike3d/map/MapGenerator.java`

調整難度常量：
```java
private static final int ROOM_COUNT = 8;        // 房間多 = 遊戲時間長
private static final int MIN_ROOM_SIZE = 5;     // 房間大 = 導航容易
private static final int FLOOR_COUNT = 18;      // 總樓層數
```

**文件**：`src/main/java/com/roguelike3d/entity/Enemy.java`

調整每層屬性增長：
```java
float floorMultiplier = 1.0f + (floor * 0.1f);  // 每層增加 10%
```

### 添加牆紋理

**文件**：`src/main/java/com/roguelike3d/renderer/RaycastRenderer.java`

替換當前的 `shade()` 方法（使用簡單顏色漸變）為紋理映射：

1. 在構造函數中加載紋理圖集為 `BufferedImage`
2. 在光線命中計算中，確定紋理坐標（u, v）
3. 採樣紋理像素並應用基於距離的變暗
4. 渲染紋理像素而不是平面顏色

**偽代碼**：
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

在 `attack()` 方法中調用：
```java
playSound("sounds/slash.wav");
```

### 持久高分追蹤

**文件**：`src/main/java/com/roguelike3d/Game.java`

在 `renderGameOver()` 中添加文件 I/O：
```java
PrintWriter writer = new PrintWriter("highscores.txt", "UTF-8");
writer.println("Player: " + player.getName() + " | Score: " + player.getScore() + 
               " | Floor: " + player.getFloor());
writer.close();
```

從 `Menu.render()` 在標題屏幕上加載並顯示。

### 自定義地牢主題

在 `ColorPalette.java` 中添加可配置調色板：
```java
public static final int[] LAVA_THEME = {0xFF4400, 0xFF6600, 0xFF8800, ...};
public static final int[] ICE_THEME = {0x00AAFF, 0x00CCFF, 0x00EEFF, ...};
```

根據當前樓層傳遞主題給 `RaycastRenderer`。

## 開發指南

### 代碼風格
- 使用清晰、描述性的變量名（`rayDistance` 而不是 `rd`）
- 保持方法專注（單一職責）
- 對魔數使用常量（見 `MapGenerator.ROOM_COUNT`）
- 記錄複雜算法的文檔（尤其是光線投射邏輯）

### 性能考慮
- **幀率**：目標 60 FPS；使用 `System.nanoTime()` 測試
- **光線投射**：每幀約 240 條光線（約 60 條/毫秒）；使用早期終止
- **內存**：典型 ~50MB；避免遊戲迴圈中的對象分配
- **優化建議**：
  - 在 `ColorPalette` 中緩存顏色計算
  - 盡可能使用整數算術而非浮點數
  - 使用 JProfiler 或內置 `-prof` 標誌進行分析

### 測試方法
1. **手動測試**：玩多局，改變樓層計數
2. **邊界情況**：測試地圖邊界、碰撞角落情況
3. **性能**：在 1920x1080 分辨率下監控幀率

### 添加測試

創建 `src/test/java/` 並添加單元測試：
```java
public class MapGeneratorTest {
    @Test
    public void testMapConnectivity() {
        DungeonMap map = MapGenerator.generate(10);
        assertTrue(isConnected(map));
    }
}
```

運行方式：
```bash
mvn test
```

## 故障排除

### 遊戲無法啟動
- **錯誤**："Main-Class not found"
  - **解決方案**：確保 `pom.xml` 有正確的 `<mainClass>` 條目
  
- **錯誤**："NoSuchMethodError"
  - **解決方案**：檢查 Java 版本是否為 17+：`java -version`

### 渲染問題
- **圖形失真**：嘗試調整窗口大小或重啟
- **啟動時黑屏**：檢查顯卡驅動；嘗試 `java -Dswing.disableBufferPerComponent=true -jar target/roguelike3d.jar`
- **閃爍/撕裂**：增加緩衝區大小或禁用 vsync（如可用）

### 性能問題
- **FPS 低**：降低屏幕分辨率或在 `Game.java` 中增加幀跳過間隔
- **內存洩漏**：檢查 `Item` 對象在收集後是否被正確垃圾回收

### 編譯失敗
- **找不到 Maven**：確保已安裝 Maven：`mvn -version`
- **源文件未找到**：驗證所有 Java 文件在 `src/main/java/` 中

## 開發路線圖

### 版本 1.1（計劃中）
- [ ] 可配置難度等級（簡單/普通/困難）
- [ ] 排行榜系統，支持持久高分
- [ ] 額外敵人類型（幽靈、石頭人）
- [ ] 更多物品類型（速度提升、臨時護盾）
- [ ] 武器多樣性（劍、弓、魔法杖）

### 版本 1.2（計劃中）
- [ ] 多人支持（本地合作，分屏模式）
- [ ] 里程碑樓層的 Boss 戰鬥
- [ ] 冷卻時間的法術/魔法系統
- [ ] 牆紋理和高級渲染效果
- [ ] 音效和環境音樂
- [ ] 通過插件架構的 Mod 支持

### 版本 2.0（未來展望）
- [ ] 網絡多人模式（與朋友在線地牢爬行）
- [ ] 高級 AI 和尋路（A* 算法）
- [ ] 故事活動和 NPC 互動
- [ ] 完整的 6-DOF 運動（向上/向下看）
- [ ] 動態陰影的光照系統
- [ ] 跨平台編譯為本地二進制（GraalVM Native Image）

## 許可證

本項目採用 **MIT 許可證** 分發。詳見 [LICENSE](LICENSE) 文件。

MIT 許可證允許在商業項目中自由使用、修改和分發，唯一要求是註明出處。

## 貢獻者

- **Jerome-zjr** — 原始作者和維護者

## 致謝

- 靈感來自經典 Roguelike 遊戲（NetHack、Dungeon Crawl）
- 光線投射算法基於 Wolfenstein 3D 技術
- 社區反饋和問題報告

---

**有問題或想貢獻？** [提交 issue](https://github.com/Jerome-zjr/java-project/issues) 或提交 pull request！
