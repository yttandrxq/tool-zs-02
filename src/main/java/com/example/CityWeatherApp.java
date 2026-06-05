package com.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 城市天气查询 — Swing 桌面应用
 * 用户选择全国任意城市，查看当天的天气情况
 */
public class CityWeatherApp extends JFrame {

    private static final long serialVersionUID = 1L;

    // ========== 城市数据：省份 → (城市名, 拼音) ==========
    private static final Map<String, String[][]> CITY_DATA = new LinkedHashMap<>();

    static {
        CITY_DATA.put("北京",     new String[][]{{"北京","beijing"}});
        CITY_DATA.put("上海",     new String[][]{{"上海","shanghai"}});
        CITY_DATA.put("天津",     new String[][]{{"天津","tianjin"}});
        CITY_DATA.put("重庆",     new String[][]{{"重庆","chongqing"}});
        CITY_DATA.put("河北",     new String[][]{{"石家庄","shijiazhuang"},{"唐山","tangshan"},{"秦皇岛","qinhuangdao"},{"邯郸","handan"},{"邢台","xingtai"},{"保定","baoding"},{"张家口","zhangjiakou"},{"承德","chengde"},{"沧州","cangzhou"},{"廊坊","langfang"},{"衡水","hengshui"}});
        CITY_DATA.put("山西",     new String[][]{{"太原","taiyuan"},{"大同","datong"},{"阳泉","yangquan"},{"长治","changzhi"},{"晋城","jincheng"},{"朔州","shuozhou"},{"晋中","jinzhong"},{"运城","yuncheng"},{"忻州","xinzhou"},{"临汾","linfen"},{"吕梁","lvliang"}});
        CITY_DATA.put("内蒙古",   new String[][]{{"呼和浩特","hohhot"},{"包头","baotou"},{"乌海","wuhai"},{"赤峰","chifeng"},{"通辽","tongliao"},{"鄂尔多斯","ordos"},{"呼伦贝尔","hulunbuir"},{"巴彦淖尔","bayannur"},{"乌兰察布","ulanqab"}});
        CITY_DATA.put("辽宁",     new String[][]{{"沈阳","shenyang"},{"大连","dalian"},{"鞍山","anshan"},{"抚顺","fushun"},{"本溪","benxi"},{"丹东","dandong"},{"锦州","jinzhou"},{"营口","yingkou"},{"阜新","fuxin"},{"辽阳","liaoyang"},{"盘锦","panjin"},{"铁岭","tieling"},{"朝阳","chaoyang-ln"},{"葫芦岛","huludao"}});
        CITY_DATA.put("吉林",     new String[][]{{"长春","changchun"},{"吉林","jilin"},{"四平","siping"},{"辽源","liaoyuan"},{"通化","tonghua"},{"白山","baishan"},{"松原","songyuan"},{"白城","baicheng"}});
        CITY_DATA.put("黑龙江",   new String[][]{{"哈尔滨","harbin"},{"齐齐哈尔","qiqihar"},{"鸡西","jixi"},{"鹤岗","hegang"},{"双鸭山","shuangyashan"},{"大庆","daqing"},{"伊春","yichun"},{"佳木斯","jiamusi"},{"七台河","qitaihe"},{"牡丹江","mudanjiang"},{"黑河","heihe"},{"绥化","suihua"}});
        CITY_DATA.put("江苏",     new String[][]{{"南京","nanjing"},{"无锡","wuxi"},{"徐州","xuzhou"},{"常州","changzhou"},{"苏州","suzhou"},{"南通","nantong"},{"连云港","lianyungang"},{"淮安","huaian"},{"盐城","yancheng"},{"扬州","yangzhou"},{"镇江","zhenjiang"},{"泰州","taizhou"},{"宿迁","suqian"}});
        CITY_DATA.put("浙江",     new String[][]{{"杭州","hangzhou"},{"宁波","ningbo"},{"温州","wenzhou"},{"嘉兴","jiaxing"},{"湖州","huzhou"},{"绍兴","shaoxing"},{"金华","jinhua"},{"衢州","quzhou"},{"舟山","zhoushan"},{"台州","taizhou-zj"},{"丽水","lishui"}});
        CITY_DATA.put("安徽",     new String[][]{{"合肥","hefei"},{"芜湖","wuhu"},{"蚌埠","bengbu"},{"淮南","huainan"},{"马鞍山","maanshan"},{"淮北","huaibei"},{"铜陵","tongling"},{"安庆","anqing"},{"黄山","huangshan"},{"滁州","chuzhou"},{"阜阳","fuyang"},{"宿州","suzhou-ah"},{"六安","luan"},{"亳州","bozhou"},{"池州","chizhou"},{"宣城","xuancheng"}});
        CITY_DATA.put("福建",     new String[][]{{"福州","fuzhou"},{"厦门","xiamen"},{"莆田","putian"},{"三明","sanming"},{"泉州","quanzhou"},{"漳州","zhangzhou"},{"南平","nanping"},{"龙岩","longyan"},{"宁德","ningde"}});
        CITY_DATA.put("江西",     new String[][]{{"南昌","nanchang"},{"景德镇","jingdezhen"},{"萍乡","pingxiang"},{"九江","jiujiang"},{"新余","xinyu"},{"鹰潭","yingtan"},{"赣州","ganzhou"},{"吉安","jian"},{"宜春","yichun-jx"},{"抚州","fuzhou-jx"},{"上饶","shangrao"}});
        CITY_DATA.put("山东",     new String[][]{{"济南","jinan"},{"青岛","qingdao"},{"淄博","zibo"},{"枣庄","zaozhuang"},{"东营","dongying"},{"烟台","yantai"},{"潍坊","weifang"},{"济宁","jining"},{"泰安","taian"},{"威海","weihai"},{"日照","rizhao"},{"临沂","linyi"},{"德州","dezhou"},{"聊城","liaocheng"},{"滨州","binzhou"},{"菏泽","heze"}});
        CITY_DATA.put("河南",     new String[][]{{"郑州","zhengzhou"},{"开封","kaifeng"},{"洛阳","luoyang"},{"平顶山","pingdingshan"},{"安阳","anyang"},{"鹤壁","hebi"},{"新乡","xinxiang"},{"焦作","jiaozuo"},{"濮阳","puyang"},{"许昌","xuchang"},{"漯河","luohe"},{"三门峡","sanmenxia"},{"南阳","nanyang"},{"商丘","shangqiu"},{"信阳","xinyang"},{"周口","zhoukou"},{"驻马店","zhumadian"}});
        CITY_DATA.put("湖北",     new String[][]{{"武汉","wuhan"},{"黄石","huangshi"},{"十堰","shiyan"},{"宜昌","yichang"},{"襄阳","xiangyang"},{"鄂州","ezhou"},{"荆门","jingmen"},{"孝感","xiaogan"},{"荆州","jingzhou"},{"黄冈","huanggang"},{"咸宁","xianning"},{"随州","suizhou"}});
        CITY_DATA.put("湖南",     new String[][]{{"长沙","changsha"},{"株洲","zhuzhou"},{"湘潭","xiangtan"},{"衡阳","hengyang"},{"邵阳","shaoyang"},{"岳阳","yueyang"},{"常德","changde"},{"张家界","zhangjiajie"},{"益阳","yiyang"},{"郴州","chenzhou"},{"永州","yongzhou"},{"怀化","huaihua"},{"娄底","loudi"}});
        CITY_DATA.put("广东",     new String[][]{{"广州","guangzhou"},{"韶关","shaoguan"},{"深圳","shenzhen"},{"珠海","zhuhai"},{"汕头","shantou"},{"佛山","foshan"},{"江门","jiangmen"},{"湛江","zhanjiang"},{"茂名","maoming"},{"肇庆","zhaoqing"},{"惠州","huizhou"},{"梅州","meizhou"},{"汕尾","shanwei"},{"河源","heyuan"},{"阳江","yangjiang"},{"清远","qingyuan"},{"东莞","dongguan"},{"中山","zhongshan"},{"潮州","chaozhou"},{"揭阳","jieyang"},{"云浮","yunfu"}});
        CITY_DATA.put("广西",     new String[][]{{"南宁","nanning"},{"柳州","liuzhou"},{"桂林","guilin"},{"梧州","wuzhou"},{"北海","beihai"},{"防城港","fangchenggang"},{"钦州","qinzhou"},{"贵港","guigang"},{"玉林","yulin"},{"百色","baise"},{"贺州","hezhou"},{"河池","hechi"},{"来宾","laibin"},{"崇左","chongzuo"}});
        CITY_DATA.put("海南",     new String[][]{{"海口","haikou"},{"三亚","sanya"},{"三沙","sansha"},{"儋州","danzhou"}});
        CITY_DATA.put("四川",     new String[][]{{"成都","chengdu"},{"自贡","zigong"},{"攀枝花","panzhihua"},{"泸州","luzhou"},{"德阳","deyang"},{"绵阳","mianyang"},{"广元","guangyuan"},{"遂宁","suining"},{"内江","neijiang"},{"乐山","leshan"},{"南充","nanchong"},{"眉山","meishan"},{"宜宾","yibin"},{"广安","guangan"},{"达州","dazhou"},{"雅安","yaan"},{"巴中","bazhong"},{"资阳","ziyang"}});
        CITY_DATA.put("贵州",     new String[][]{{"贵阳","guiyang"},{"六盘水","liupanshui"},{"遵义","zunyi"},{"安顺","anshun"},{"毕节","bijie"},{"铜仁","tongren"}});
        CITY_DATA.put("云南",     new String[][]{{"昆明","kunming"},{"曲靖","qujing"},{"玉溪","yuxi"},{"保山","baoshan"},{"昭通","zhaotong"},{"丽江","lijiang"},{"普洱","puer"},{"临沧","lincang"}});
        CITY_DATA.put("西藏",     new String[][]{{"拉萨","lhasa"},{"日喀则","shigatse"},{"昌都","chamdo"},{"林芝","nyingchi"},{"山南","shannan"},{"那曲","naqu"}});
        CITY_DATA.put("陕西",     new String[][]{{"西安","xian"},{"铜川","tongchuan"},{"宝鸡","baoji"},{"咸阳","xianyang"},{"渭南","weinan"},{"延安","yanan"},{"汉中","hanzhong"},{"榆林","yulin-sn"},{"安康","ankang"},{"商洛","shangluo"}});
        CITY_DATA.put("甘肃",     new String[][]{{"兰州","lanzhou"},{"嘉峪关","jiayuguan"},{"金昌","jinchang"},{"白银","baiyin"},{"天水","tianshui"},{"武威","wuwei"},{"张掖","zhangye"},{"平凉","pingliang"},{"酒泉","jiuquan"},{"庆阳","qingyang"},{"定西","dingxi"},{"陇南","longnan"}});
        CITY_DATA.put("青海",     new String[][]{{"西宁","xining"},{"海东","haidong"}});
        CITY_DATA.put("宁夏",     new String[][]{{"银川","yinchuan"},{"石嘴山","shizuishan"},{"吴忠","wuzhong"},{"固原","guyuan"},{"中卫","zhongwei"}});
        CITY_DATA.put("新疆",     new String[][]{{"乌鲁木齐","urumqi"},{"克拉玛依","karamay"},{"吐鲁番","turpan"},{"哈密","hami"}});
        CITY_DATA.put("香港",     new String[][]{{"香港","hongkong"}});
        CITY_DATA.put("澳门",     new String[][]{{"澳门","macau"}});
        CITY_DATA.put("台湾",     new String[][]{{"台北","taipei"},{"高雄","kaohsiung"},{"台中","taichung"},{"台南","tainan"}});
    }

    // ========== 全局变量 ==========
    private static final Color BG_COLOR   = new Color(245, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color ACCENT     = new Color(41, 128, 185);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    private JComboBox<String> provinceBox;
    private JComboBox<String> cityBox;
    private JButton           queryBtn;
    private JLabel            statusLabel;
    private JPanel            weatherPanel;
    private JLabel            weatherIconLabel;
    private JLabel            tempLabel;
    private JLabel            descLabel;
    private JLabel            detailLabel;

    public CityWeatherApp() {
        initUI();
        setTitle("城市天气查询");
        setSize(520, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
    }

    // ---------- 构建界面 ----------
    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(BG_COLOR);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ---- 顶部：标题 ----
        JLabel titleLabel = new JLabel("城市天气查询", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT);
        root.add(titleLabel, BorderLayout.NORTH);

        // ---- 中间主体 ----
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);

        center.add(buildSelectorPanel(), BorderLayout.NORTH);
        center.add(buildWeatherPanel(), BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        // ---- 底部状态栏 ----
        statusLabel = new JLabel("请选择省份和城市，然后点击「查询天气」");
        statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(127, 140, 141));
        statusLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        root.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ---------- 城市选择区 ----------
    private JPanel buildSelectorPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 省份
        JLabel pLabel = new JLabel("省  份：");
        pLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        card.add(pLabel, gbc);

        provinceBox = new JComboBox<>(CITY_DATA.keySet().toArray(new String[0]));
        provinceBox.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        provinceBox.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        card.add(provinceBox, gbc);

        // 城市
        JLabel cLabel = new JLabel("城  市：");
        cLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        card.add(cLabel, gbc);

        cityBox = new JComboBox<>();
        cityBox.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        cityBox.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        card.add(cityBox, gbc);

        // 按钮
        queryBtn = new JButton("查询天气");
        queryBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        queryBtn.setBackground(ACCENT);
        queryBtn.setForeground(Color.WHITE);
        queryBtn.setFocusPainted(false);
        queryBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        queryBtn.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        card.add(queryBtn, gbc);

        // ---- 事件 ----
        provinceBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshCityBox((String) provinceBox.getSelectedItem());
            }
        });
        refreshCityBox((String) provinceBox.getSelectedItem());

        queryBtn.addActionListener(e -> queryWeather());

        return card;
    }

    private void refreshCityBox(String province) {
        cityBox.removeAllItems();
        if (province == null) return;
        String[][] cities = CITY_DATA.get(province);
        if (cities != null) {
            for (String[] c : cities) cityBox.addItem(c[0]);
        }
    }

    // ---------- 天气展示区 ----------
    private JPanel buildWeatherPanel() {
        weatherPanel = new JPanel(new GridBagLayout());
        weatherPanel.setBackground(CARD_COLOR);
        weatherPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true),
                        "今日天气", TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Microsoft YaHei", Font.BOLD, 14), ACCENT),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 10, 5, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        // 默认显示引导文字
        weatherIconLabel = new JLabel("☁  ", JLabel.CENTER);
        weatherIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.fill = GridBagConstraints.CENTER;
        weatherPanel.add(weatherIconLabel, g);

        tempLabel = new JLabel("请选择城市查询", JLabel.CENTER);
        tempLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        tempLabel.setForeground(new Color(149, 165, 166));
        g.gridy = 1; g.insets = new Insets(0, 10, 0, 10);
        weatherPanel.add(tempLabel, g);

        descLabel = new JLabel("", JLabel.CENTER);
        descLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_COLOR);
        g.gridy = 2;
        weatherPanel.add(descLabel, g);

        detailLabel = new JLabel("", JLabel.CENTER);
        detailLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        detailLabel.setForeground(new Color(127, 140, 141));
        g.gridy = 3;
        weatherPanel.add(detailLabel, g);

        return weatherPanel;
    }

    // ======================== 天气查询逻辑 ========================

    private void queryWeather() {
        String cityName = (String) cityBox.getSelectedItem();
        if (cityName == null || cityName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先选择一个城市！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pinyin = getPinyin(cityName);
        if (pinyin == null || pinyin.isEmpty()) {
            statusLabel.setText("未找到该城市的拼音映射，请检查城市数据");
            return;
        }

        queryBtn.setEnabled(false);
        statusLabel.setText("正在查询 " + cityName + " 的天气……");

        new Thread(() -> {
            try {
                JSONObject data = fetchWeather(pinyin);
                SwingUtilities.invokeLater(() -> {
                    updateWeatherDisplay(cityName, data);
                    statusLabel.setText(cityName + " 天气已更新  ·  " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    queryBtn.setEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("查询失败：" + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "天气查询失败，请检查网络连接后重试。\n错误：" + ex.getMessage(), "查询失败", JOptionPane.ERROR_MESSAGE);
                    queryBtn.setEnabled(true);
                });
            }
        }).start();
    }

    /**
     * 调用 wttr.in API 获取天气 JSON
     */
    private JSONObject fetchWeather(String pinyin) throws Exception {
        String url = "https://wttr.in/" + URLEncoder.encode(pinyin, "UTF-8") + "?format=j1&lang=zh";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new RuntimeException("HTTP " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        conn.disconnect();
        return new JSONObject(sb.toString());
    }

    /**
     * 将 API 返回的数据渲染到界面
     */
    private void updateWeatherDisplay(String cityName, JSONObject data) {
        try {
            JSONArray currentArr = data.optJSONArray("current_condition");
            if (currentArr == null || currentArr.length() == 0) {
                throw new RuntimeException("未获取到天气数据");
            }
            JSONObject current = currentArr.getJSONObject(0);

            String tempC     = current.optString("temp_C", "--");
            String feelsLike = current.optString("FeelsLikeC", "--");
            String humidity  = current.optString("humidity", "--");
            String windSpeed = current.optString("windspeedKmph", "--");
            String windDir   = current.optString("winddir16Point", "--");
            String visibility= current.optString("visibility", "--");
            String pressure  = current.optString("pressure", "--");
            String uvIndex   = current.optString("uvIndex", "--");

            JSONArray descArr = current.optJSONArray("weatherDesc");
            String weatherDesc = "未知";
            if (descArr != null && descArr.length() > 0) {
                weatherDesc = descArr.getJSONObject(0).optString("value", "未知");
            }

            // 天文数据（日出、日落）
            JSONArray weatherArr = data.optJSONArray("weather");
            String sunrise = "--", sunset = "--", maxTemp = "--", minTemp = "--";
            if (weatherArr != null && weatherArr.length() > 0) {
                JSONObject today = weatherArr.getJSONObject(0);
                JSONArray astro = today.optJSONArray("astronomy");
                if (astro != null && astro.length() > 0) {
                    sunrise = astro.getJSONObject(0).optString("sunrise", "--");
                    sunset  = astro.getJSONObject(0).optString("sunset", "--");
                }
                maxTemp = today.optString("maxtempC", "--");
                minTemp = today.optString("mintempC", "--");
            }

            // 天气图标
            String icon = getWeatherIcon(weatherDesc);
            weatherIconLabel.setText(icon);
            weatherIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));

            // 城市名 + 温度
            tempLabel.setText(cityName + "  " + tempC + "°C");
            tempLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));
            tempLabel.setForeground(ACCENT);

            // 描述
            descLabel.setText(weatherDesc + "  |  体感 " + feelsLike + "°C  |  " + minTemp + "°C ~ " + maxTemp + "°C");
            descLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
            descLabel.setForeground(TEXT_COLOR);

            // 详细信息
            String detail = String.format(
                "湿度: %s%%  |  风速: %s km/h (%s)  |  能见度: %s km  |  气压: %s hPa  |  紫外线: %s  |  日出: %s  日落: %s",
                humidity, windSpeed, windDir, visibility, pressure, uvIndex, sunrise, sunset
            );
            detailLabel.setText("<html><div style='text-align:center; line-height:1.8;'>" + detail + "</div></html>");
            detailLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));

        } catch (Exception e) {
            weatherIconLabel.setText("\u26A0");
            tempLabel.setText("数据解析失败");
            tempLabel.setForeground(new Color(231, 76, 60));
            descLabel.setText(e.getMessage());
            detailLabel.setText("");
        }
    }

    /**
     * 根据天气描述返回对应的 emoji
     */
    private String getWeatherIcon(String desc) {
        String d = desc.toLowerCase();
        if (d.contains("晴") || d.contains("sunny") || d.contains("clear"))     return "\u2600\uFE0F  ";
        if (d.contains("云") || d.contains("cloud") || d.contains("overcast"))  return "\u26C5  ";
        if (d.contains("阴"))                                                    return "\u2601\uFE0F  ";
        if (d.contains("雾") || d.contains("mist") || d.contains("fog") || d.contains("haze")) return "\uD83C\uDF2B\uFE0F  ";
        if (d.contains("雪") || d.contains("snow"))                             return "\u2744\uFE0F  ";
        if (d.contains("雷") || d.contains("thunder"))                          return "\u26C8\uFE0F  ";
        if (d.contains("雨") || d.contains("rain") || d.contains("drizzle") || d.contains("shower")) return "\uD83C\uDF27\uFE0F  ";
        if (d.contains("霾") || d.contains("沙") || d.contains("尘"))            return "\uD83C\uDF2B\uFE0F  ";
        return "\u2600\uFE0F  ";
    }

    /**
     * 根据城市中文名查找对应拼音
     */
    private String getPinyin(String cityName) {
        for (String[][] cities : CITY_DATA.values()) {
            for (String[] c : cities) {
                if (c[0].equals(cityName)) return c[1];
            }
        }
        return null;
    }

    // ======================== 入口 ========================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new CityWeatherApp().setVisible(true));
    }
}
