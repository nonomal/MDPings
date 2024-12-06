## MDPings

[![Channel](https://img.shields.io/badge/Telegram-Channel-blue?style=flat-square&logo=telegram)](https://t.me/mdpings_app)

MDPings 是一个基于哪吒监控 API 接口开发的 MD3 风格 Android 客户端，支持同时监控多个服务器的状态，提供历史网络状态和延迟图表

## 界面

### 主页（平板）

<p style="text-align: center;">
    <img alt="desktop" src="snapshots/Tablet_Screenshot_Home_Portrait_Night.png" width="720">
</p>

<p style="text-align: center;">
    <img alt="desktop" src="snapshots/Tablet_Screenshot_Home_Landscape_Light.png" width="720">
</p>

### 主页（手机）

<p style="text-align: center;">
    <img alt="desktop" src="snapshots/Screenshot_Light.png" width="360"> <img alt="desktop" src="snapshots/Screenshot_Night.png" width="360">
</p>

### 详情页（手机）

<p style="text-align: center;">
    <img alt="desktop" src="snapshots/Screenshot_ServerDetail_Night.png" width="360"> <img alt="desktop" src="snapshots/Screenshot_Monitors_Night.png" width="360">
</p>

### 其他

<p style="text-align: center;">
    <img alt="desktop" src="snapshots/Screenshot_Login_Night.png" width="360"> <img alt="desktop" src="snapshots/Screenshot_Visual_Light.png" width="360">
</p>

## Features

✈️ 多后端切换

🖥️ 总流量监控

💡 MD3风格、自适应布局

## Download

<a href="https://github.com/icylian/MDPings/releases"><img alt="Get it on GitHub" src="snapshots/get-it-on-github.svg" width="200px"/></a>

## 常见问题

#### 1.支持的哪吒监控版本？

`MDPings 1.0.4(L)` 版本对应支持 哪吒监控v0 `0.20.13+` 版本，版本号带`L`的版本修改了包名，可以跟v1共存。

`MDPings 1.1.0+` 版本对应支持 哪吒监控v1 `1.0.21`。

因为目前还有挺多人在 `v0版本` 观望，因此 `v0版本` 将不会立刻舍弃，部分能力范围内能同步过去的ui/功能更新会在有心情的时候做，主要还是以适配 v1 的新接口为主。

#### 2.应用闪退了

请参照第一点，可以尝试更新哪吒监控的版本到 MDPings 对应支持的版本。

#### 3.点击 Test 后弹出 TOAST 显示 `未知错误/Something went wrong, please try again later`.

对于ssl证书新加密不兼容问题已修复，感谢 tg@呆呆 提供的 debug 用实例。
如果你使用的是 http 明文链接，换用 https 即可解决，后续会看看要不要支持 http （危）。

#### 4.添加完实例之后卡在加载服务器

哪吒监控的v1版本 由于换用了 websocket ，反代等网络配置不一可能会出现这种问题，在不提供日志/debug用测试面板的情况下没办法debug，所以如果你遇到了这种情况的话，
可以选择抓log进群丢给我或者建一个脱敏实例账号密码丢给我测试，没有debug环境就没有debug。

#### 5.API BACKEND一项的格式？

如空白截图显示，`https://your.nezha.api.com/` ，一般来说就是你的面板地址。

> [!IMPORTANT]
> 请 v0版本 不要漏掉链接里最后的斜杠，否则 Test 将无法通过，v1版本会自适应，加不加都没所谓。

## TODO

- [x] 对接哪吒监控v1

- [x] 横屏/平板适配

- [x] 日间/夜间/主题色切换

- [x] 统一界面语言、多语言支持

- [ ] 桌面小部件

- [ ] 主页列表卡片内容优化/重做

- [ ] 排序筛选功能（静候官方1.0版更新后适配）

## 支持

1.点击页面顶部的星标（⭐）

2.Push我OVH传家宝 KS-LE-B 2*1.92t 中奖鸡（？）

3.提出UI界面设计、交互操控的改进意见（最好能带示意图）

4.指点一下我的kotlin屎山

等等等等……都是我继续改进MDPings的动力，谢谢你。
