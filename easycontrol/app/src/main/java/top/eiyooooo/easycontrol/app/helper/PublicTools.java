package top.eiyooooo.easycontrol.app.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbDevice;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.DhcpInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.eiyooooo.easycontrol.app.R;
import top.eiyooooo.easycontrol.app.WebViewActivity;
import top.eiyooooo.easycontrol.app.adb.Adb;
import top.eiyooooo.easycontrol.app.client.Client;
import top.eiyooooo.easycontrol.app.databinding.*;
import top.eiyooooo.easycontrol.app.entity.AppData;
import top.eiyooooo.easycontrol.app.entity.Device;

public class PublicTools {

  // 设置全面屏
  public static void setFullScreen(Activity context) {
    // 全屏显示
    context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    context.getWindow().getDecorView().setSystemUiVisibility(
      View.SYSTEM_UI_FLAG_FULLSCREEN |
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
      View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
      View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
      View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
      View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    // 设置异形屏
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      WindowManager.LayoutParams lp = context.getWindow().getAttributes();
      lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
      context.getWindow().setAttributes(lp);
    }
  }

  // 设置语言
  public static void setLocale(Activity context) {
    Resources resources = context.getResources();
    Configuration config = resources.getConfiguration();
    String locale = AppData.setting.getDefaultLocale();
    if (locale.equals("")) config.locale = Locale.getDefault();
    else if (locale.equals("en")) config.locale = Locale.ENGLISH;
    else if (locale.equals("zh")) config.locale = Locale.CHINESE;
    resources.updateConfiguration(config, resources.getDisplayMetrics());
  }

  // 设置状态栏导航栏颜色
  public static void setStatusAndNavBar(Activity context) {
    // 导航栏
    context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.background));
    // 状态栏
    context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.background));
    if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_YES)
      context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
  }

  // DP转PX
  public static int dp2px(Float dp) {
    return (int) (dp * AppData.realScreenSize.density);
  }

  // 创建弹窗
  public static Dialog createDialog(Context context, boolean canCancel, View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setCancelable(true);
    ScrollView dialogView = ModuleDialogBinding.inflate(LayoutInflater.from(context)).getRoot();
    dialogView.addView(view);
    dialogView.setPadding(0, 0, 0, 0); // 设置内边距为0
    builder.setView(dialogView);
    Dialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(canCancel);
    dialog.setCancelable(canCancel);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    return dialog;
  }

  public static void showNightModeChanger(Context context, Device device) {
    String currentNightMode = getCurrentNightMode(context, device);
    ItemNightModeChangerBinding nightModeChangerView = ItemNightModeChangerBinding.inflate(LayoutInflater.from(context));
    nightModeChangerView.title.setText(currentNightMode);
    nightModeChangerView.buttonAuto.setOnClickListener(v -> {
      try {
        if (Adb.getStringResponseFromServer(device, "setNightMode", "nightMode=0").contains("success")) {
          PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_success));
        } else throw new Exception();
      } catch (Exception ignored) {
        PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_failed));
      }
      nightModeChangerView.title.setText(getCurrentNightMode(context, device));
    });
    nightModeChangerView.buttonCustom.setOnClickListener(v -> {
      try {
        if (Adb.getStringResponseFromServer(device, "setNightMode", "nightMode=3").contains("success")) {
          PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_success));
        } else throw new Exception();
      } catch (Exception ignored) {
        PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_failed));
      }
      nightModeChangerView.title.setText(getCurrentNightMode(context, device));
    });
    nightModeChangerView.buttonYes.setOnClickListener(v -> {
      try {
        if (Adb.getStringResponseFromServer(device, "setNightMode", "nightMode=2").contains("success")) {
          PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_success));
        } else throw new Exception();
      } catch (Exception ignored) {
        PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_failed));
      }
      nightModeChangerView.title.setText(getCurrentNightMode(context, device));
    });
    nightModeChangerView.buttonNo.setOnClickListener(v -> {
      try {
        if (Adb.getStringResponseFromServer(device, "setNightMode", "nightMode=1").contains("success")) {
          PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_success));
        } else throw new Exception();
      } catch (Exception ignored) {
        PublicTools.logToast(AppData.main.getString(R.string.change_night_mode_failed));
      }
      nightModeChangerView.title.setText(getCurrentNightMode(context, device));
    });
    Dialog nightModeChangerDialog = PublicTools.createDialog(context, true, nightModeChangerView.getRoot());
    nightModeChangerDialog.show();
  }

  private static String getCurrentNightMode(Context context, Device device) {
    String currentNightMode = context.getString(R.string.night_mode_current);
    try {
      switch (Integer.parseInt(Adb.getStringResponseFromServer(device, "getNightMode"))) {
        case 0:
          currentNightMode = currentNightMode + context.getString(R.string.night_mode_auto);
          break;
        case 1:
          currentNightMode = currentNightMode + context.getString(R.string.night_mode_no);
          break;
        case 2:
          currentNightMode = currentNightMode + context.getString(R.string.night_mode_yes);
          break;
        case 3:
          currentNightMode = currentNightMode + context.getString(R.string.night_mode_custom);
          break;
      }
    } catch (Exception ignored) {
      currentNightMode = context.getString(R.string.set_device_button_night_mode);
    }
    return currentNightMode;
  }

  // 创建新建设备弹窗
  public static Dialog createAddDeviceView(
    Context context,
    Device device,
    DeviceListAdapter deviceListAdapter
  ) {
    ItemAddDeviceBinding itemAddDeviceBinding = ItemAddDeviceBinding.inflate(LayoutInflater.from(context));
    Dialog dialog = createDialog(context, true, itemAddDeviceBinding.getRoot());
    // 设置值
    itemAddDeviceBinding.name.setText(device.name);
    itemAddDeviceBinding.address.setText(device.address);
    itemAddDeviceBinding.specifiedApp.setText(device.specified_app);
    // 创建View
    createDeviceOptionSet(context, itemAddDeviceBinding.options, device);
    // 特殊设备不允许修改
    if (!device.isNormalDevice()) {
      itemAddDeviceBinding.addressTitle.setVisibility(View.GONE);
      itemAddDeviceBinding.address.setVisibility(View.GONE);
      itemAddDeviceBinding.scanAddress.setVisibility(View.GONE);
    }
    // 是否显示高级选项
    itemAddDeviceBinding.isOptions.setOnClickListener(v -> itemAddDeviceBinding.options.setVisibility(itemAddDeviceBinding.isOptions.isChecked() ? View.VISIBLE : View.GONE));
    // 扫描按钮监听
    itemAddDeviceBinding.scanAddress.setOnClickListener(v -> {
      itemAddDeviceBinding.addressTitle.setText(context.getString(R.string.add_device_scanning));
      itemAddDeviceBinding.scanAddress.setEnabled(false);
      new Thread(() -> {
        ArrayList<String> scannedAddresses = scanAddress();
        AppData.uiHandler.post(() -> {
          if (scannedAddresses.isEmpty()) Toast.makeText(context, context.getString(R.string.add_device_scan_address_finish_none), Toast.LENGTH_SHORT).show();
          else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.add_device_scan_finish));
            builder.setItems(scannedAddresses.toArray(new String[0]), (dialog1, which) -> {
              String address = scannedAddresses.get(which);
              itemAddDeviceBinding.address.setText(address);
            });
            builder.show();
          }
          itemAddDeviceBinding.addressTitle.setText(context.getString(R.string.add_device_address));
          itemAddDeviceBinding.scanAddress.setEnabled(true);
        });
      }).start();
    });
    itemAddDeviceBinding.scanRemoteAppList.setOnClickListener(v -> {
      UsbDevice usbDevice = DeviceListAdapter.linkDevices.get(device.uuid);
      if (device.isLinkDevice() && usbDevice == null) return;
      if (device.type == Device.TYPE_NORMAL && !String.valueOf(itemAddDeviceBinding.address.getText()).isEmpty()) device.address = String.valueOf(itemAddDeviceBinding.address.getText());
      itemAddDeviceBinding.specifiedAppTitle.setText(context.getString(R.string.add_device_scanning));
      itemAddDeviceBinding.scanRemoteAppList.setEnabled(false);
      new Thread(() -> {
        ArrayList<String> remoteAppList = Client.getAppList(device, device.isLinkDevice() ? usbDevice : null);
        AppData.uiHandler.post(() -> {
          if (remoteAppList.isEmpty()) Toast.makeText(context, context.getString(R.string.add_device_scan_specify_app_finish_error), Toast.LENGTH_SHORT).show();
          else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.add_device_scan_finish));
            builder.setItems(remoteAppList.toArray(new String[0]), (dialog1, which) -> {
              String app = remoteAppList.get(which);
              if (app.contains("@")) app = app.split("@")[1];
              itemAddDeviceBinding.specifiedApp.setText(app);
            });
            builder.show();
          }
          itemAddDeviceBinding.specifiedAppTitle.setText(context.getString(R.string.add_device_specify_app));
          itemAddDeviceBinding.scanRemoteAppList.setEnabled(true);
        });
      }).start();
    });
    // 设置确认按钮监听
    itemAddDeviceBinding.ok.setOnClickListener(v -> {
      if (device.type == Device.TYPE_NORMAL) {
        if (String.valueOf(itemAddDeviceBinding.address.getText()).isEmpty()) return;
        else if (Adb.adbMap.containsKey(device.uuid) && !Objects.equals(device.address, String.valueOf(itemAddDeviceBinding.address.getText()))) {
          Objects.requireNonNull(Adb.adbMap.get(device.uuid)).close();
        }
      }
      device.name = String.valueOf(itemAddDeviceBinding.name.getText());
      device.address = String.valueOf(itemAddDeviceBinding.address.getText());
      device.specified_app = String.valueOf(itemAddDeviceBinding.specifiedApp.getText());
      if (AppData.dbHelper.getByUUID(device.uuid) != null) AppData.dbHelper.update(device);
      else AppData.dbHelper.insert(device);
      deviceListAdapter.update();
      dialog.cancel();
    });
    return dialog;
  }

  // 创建设备参数设置页面
  private static final String[] maxFpsList = new String[]{"90", "60", "40", "30", "20", "10"};
  private static final String[] maxVideoBitList = new String[]{"12", "8", "4", "2", "1"};

  public static void createDeviceOptionSet(Context context, ViewGroup fatherLayout, Device device) {
    // Device为null，则视为设置默认参数
    boolean setDefault = device == null;
    // 数组适配器
    ArrayAdapter<String> maxSizeAdapter = new ArrayAdapter<>(AppData.main, R.layout.item_spinner_item, new String[]{context.getString(R.string.option_max_size_original), "2560", "1920", "1600", "1280", "1024", "800"});
    ArrayAdapter<String> maxFpsAdapter = new ArrayAdapter<>(AppData.main, R.layout.item_spinner_item, maxFpsList);
    ArrayAdapter<String> maxVideoBitAdapter = new ArrayAdapter<>(AppData.main, R.layout.item_spinner_item, maxVideoBitList);
    // 添加参数视图
    fatherLayout.addView(createSpinnerCard(context, context.getString(R.string.option_max_size), context.getString(R.string.option_max_size_detail), String.valueOf(setDefault ? AppData.setting.getDefaultMaxSize() : device.maxSize), maxSizeAdapter, str -> {
      if (str.equals(context.getString(R.string.option_max_size_original))) str = "0";
      if (setDefault) AppData.setting.setDefaultMaxSize(Integer.parseInt(str));
      else device.maxSize = Integer.parseInt(str);
    }).getRoot());
    fatherLayout.addView(createSpinnerCard(context, context.getString(R.string.option_max_fps), context.getString(R.string.option_max_fps_detail), String.valueOf(setDefault ? AppData.setting.getDefaultMaxFps() : device.maxFps), maxFpsAdapter, str -> {
      if (setDefault) AppData.setting.setDefaultMaxFps(Integer.parseInt(str));
      else device.maxFps = Integer.parseInt(str);
    }).getRoot());
    fatherLayout.addView(createSpinnerCard(context, context.getString(R.string.option_max_video_bit), context.getString(R.string.option_max_video_bit_detail), String.valueOf(setDefault ? AppData.setting.getDefaultMaxVideoBit() : device.maxVideoBit), maxVideoBitAdapter, str -> {
      if (setDefault) AppData.setting.setDefaultMaxVideoBit(Integer.parseInt(str));
      else device.maxVideoBit = Integer.parseInt(str);
    }).getRoot());
    if (device != null) {
      if (device.isNormalDevice())
        fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_startup_device), context.getString(R.string.option_startup_device_detail), device.connectOnStart, isChecked -> device.connectOnStart = isChecked).getRoot());
      else if (device.isLinkDevice())
        fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_default_usb_device), context.getString(R.string.option_default_usb_device_detail), device.connectOnStart, isChecked -> device.connectOnStart = isChecked).getRoot());
    }
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_is_audio), context.getString(R.string.option_is_audio_detail), setDefault ? AppData.setting.getDefaultIsAudio() : device.isAudio, isChecked -> {
      if (setDefault) AppData.setting.setDefaultIsAudio(isChecked);
      else device.isAudio = isChecked;
    }).getRoot());
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_clipboard_sync), context.getString(R.string.option_clipboard_sync_detail), setDefault ? AppData.setting.getDefaultClipboardSync() : device.clipboardSync, isChecked -> {
      if (setDefault) AppData.setting.setDefaultClipboardSync(isChecked);
      else device.clipboardSync = isChecked;
    }).getRoot());
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_night_mode_sync), context.getString(R.string.option_night_mode_sync_detail), setDefault ? AppData.setting.getDefaultNightModeSync() : device.nightModeSync, isChecked -> {
      if (setDefault) AppData.setting.setDefaultNightModeSync(isChecked);
      else device.nightModeSync = isChecked;
    }).getRoot());
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_use_h265), context.getString(R.string.option_use_h265_detail), setDefault ? AppData.setting.getDefaultUseH265() : device.useH265, isChecked -> {
      if (setDefault) AppData.setting.setDefaultUseH265(isChecked);
      else device.useH265 = isChecked;
    }).getRoot());
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_use_opus), context.getString(R.string.option_use_opus_detail), setDefault ? AppData.setting.getDefaultUseOpus() : device.useOpus, isChecked -> {
      if (setDefault) AppData.setting.setDefaultUseOpus(isChecked);
      else device.useOpus = isChecked;
    }).getRoot());
    fatherLayout.addView(createSwitchCard(context, context.getString(R.string.option_set_resolution), context.getString(R.string.option_set_resolution_detail), setDefault ? AppData.setting.getDefaultSetResolution() : device.setResolution, isChecked -> {
      if (setDefault) AppData.setting.setDefaultSetResolution(isChecked);
      else device.setResolution = isChecked;
    }).getRoot());
  }

  // 创建Client加载框
  public static Pair<View, WindowManager.LayoutParams> createLoading(Context context) {
    ItemLoadingBinding loadingView = ItemLoadingBinding.inflate(LayoutInflater.from(context));
    WindowManager.LayoutParams loadingViewParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
    );
    loadingViewParams.gravity = Gravity.CENTER;
    return new Pair<>(loadingView.getRoot(), loadingViewParams);
  }

  // 创建纯文本卡片
  public static ItemTextBinding createTextCard(
    Context context,
    String text,
    MyFunction function
  ) {
    ItemTextBinding textView = ItemTextBinding.inflate(LayoutInflater.from(context));
    textView.getRoot().setText(text);
    if (function != null) textView.getRoot().setOnClickListener(v -> function.run());
    return textView;
  }

  // 创建纯文本带说明卡片
  public static ItemTextDetailBinding createTextCardDetail(
          Context context,
          String text,
          String textDetail,
          MyFunction function
  ) {
    ItemTextDetailBinding textDetailView = ItemTextDetailBinding.inflate(LayoutInflater.from(context));
    textDetailView.itemText.setText(text);
    textDetailView.itemDetail.setText(textDetail);
    if (function != null) textDetailView.getRoot().setOnClickListener(v -> function.run());
    return textDetailView;
  }

  // 创建开关卡片
  public static ItemSwitchBinding createSwitchCard(
    Context context,
    String text,
    String textDetail,
    boolean config,
    MyFunctionBoolean function
  ) {
    ItemSwitchBinding switchView = ItemSwitchBinding.inflate(LayoutInflater.from(context));
    switchView.itemText.setText(text);
    switchView.itemDetail.setText(textDetail);
    switchView.itemSwitch.setChecked(config);
    if (function != null) switchView.itemSwitch.setOnCheckedChangeListener((buttonView, checked) -> function.run(checked));
    return switchView;
  }

  // 创建拓展版开关卡片
  public static ItemSwitchBinding createSwitchCardEx(
    Context context,
    String text,
    String textDetail,
    boolean config,
    MyFunctionButtonBoolean function
  ) {
    ItemSwitchBinding switchView = ItemSwitchBinding.inflate(LayoutInflater.from(context));
    switchView.itemText.setText(text);
    switchView.itemDetail.setText(textDetail);
    switchView.itemSwitch.setChecked(config);
    if (function != null) switchView.itemSwitch.setOnCheckedChangeListener(function::run);
    return switchView;
  }

  // 创建列表卡片
  public static ItemSpinnerBinding createSpinnerCard(
    Context context,
    String text,
    String textDetail,
    String config,
    ArrayAdapter<String> adapter,
    MyFunctionString function
  ) {
    ItemSpinnerBinding spinnerView = ItemSpinnerBinding.inflate(LayoutInflater.from(context));
    spinnerView.itemText.setText(text);
    spinnerView.itemDetail.setText(textDetail);
    spinnerView.itemSpinner.setAdapter(adapter);
    spinnerView.itemSpinner.setSelection(adapter.getPosition(config));
    spinnerView.itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (function != null)
          function.run(spinnerView.itemSpinner.getSelectedItem().toString());
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
    return spinnerView;
  }

  // 分离地址和端口号
  public static Pair<String, Integer> getIpAndPort(String address) throws IOException {
    String pattern;
    int type;
    // 特殊格式
    if (address.contains("*")) {
      type = 2;
      pattern = "(\\*.*?\\*.*):(\\d+)";
    }
    // IPv6
    else if (address.contains("[")) {
      type = 6;
      pattern = "(\\[.*?]):(\\d+)";
    }
    // 域名
    else if (Pattern.matches(".*[a-zA-Z].*", address)) {
      type = 1;
      pattern = "(.*?):(\\d+)";
    }
    // IPv4
    else {
      type = 4;
      pattern = "(.*?):(\\d+)";
    }
    Matcher matcher = Pattern.compile(pattern).matcher(address);
    if (!matcher.find()) throw new IOException(AppData.main.getString(R.string.error_address_error));
    String ip = matcher.group(1);
    String port = matcher.group(2);
    if (ip == null || port == null) throw new IOException(AppData.main.getString(R.string.error_address_error));
    // 特殊格式
    if (type == 2) {
      if (ip.equals("*gateway*")) ip = getGateway();
      if (ip.contains("*netAddress*")) ip = ip.replace("*netAddress*", getNetAddress());
    }
    // 域名解析
    else if (type == 1) {
      ip = InetAddress.getByName(ip).getHostAddress();
    }
    return new Pair<>(ip, Integer.parseInt(port));
  }

  // 获取IP地址
  public static Pair<ArrayList<String>, ArrayList<String>> getIp() {
    ArrayList<String> ipv4Addresses = new ArrayList<>();
    ArrayList<String> ipv6Addresses = new ArrayList<>();
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress inetAddress = inetAddresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            if (inetAddress instanceof Inet4Address) ipv4Addresses.add(inetAddress.getHostAddress());
            else if (inetAddress instanceof Inet6Address && !inetAddress.isLinkLocalAddress()) ipv6Addresses.add("[" + inetAddress.getHostAddress() + "]");
          }
        }
      }
    } catch (Exception ignored) {
    }
    return new Pair<>(ipv4Addresses, ipv6Addresses);
  }

  // 获取网关地址
  public static String getGateway() {
    int ip = AppData.wifiManager.getDhcpInfo().gateway;
    // 没有wifi时，设置为1.1.1.1
    if (ip == 0) ip = 16843009;
    return decodeIntToIp(ip, 4);
  }

  // 获取子网地址
  public static String getNetAddress() {
    DhcpInfo dhcpInfo = AppData.wifiManager.getDhcpInfo();
    int gateway = dhcpInfo.gateway;
    int ipAddress = dhcpInfo.ipAddress;
    if (ipAddress == 0) {
      ipAddress = 16843009;
      return decodeIntToIp(ipAddress, 4);
    }
    // 因为dhcpInfo.netmask兼容性不好，部分设备获取值为0，所以此处使用对比方法
    int len;
    if (((gateway >> 8) & 0xff) == ((ipAddress >> 8) & 0xff)) len = 3;
    else if (((gateway >> 16) & 0xff) == ((ipAddress >> 16) & 0xff)) len = 2;
    else len = 1;
    return decodeIntToIp(gateway, len);
  }

  // 解析地址
  private static String decodeIntToIp(int ip, int len) {
    if (len < 1 || len > 4) return "";
    StringBuilder builder = new StringBuilder();
    builder.append(ip & 0xff);
    if (len > 1) {
      builder.append(".");
      builder.append((ip >> 8) & 0xff);
      if (len > 2) {
        builder.append(".");
        builder.append((ip >> 16) & 0xff);
        if (len > 3) {
          builder.append(".");
          builder.append((ip >> 24) & 0xff);
        }
      }
    }
    return builder.toString();
  }

  // 扫描局域网设备
  public static ArrayList<String> scanAddress() {
    ArrayList<String> scannedAddresses = new ArrayList<>();
    ExecutorService executor = Executors.newFixedThreadPool(256);
    ArrayList<String> ipv4List = getIp().first;
    for (String ipv4 : ipv4List) {
      Matcher matcher = Pattern.compile("(\\d+\\.\\d+\\.\\d+)").matcher(ipv4);
      if (matcher.find()) {
        String subnet = matcher.group(1);
        for (int i = 1; i <= 255; i++) {
          String host = subnet + "." + i;
          executor.execute(() -> {
            try {
              Socket socket = new Socket();
              socket.connect(new InetSocketAddress(host, 5555), 800);
              socket.close();
              // 忽略本机
              if (!host.equals(ipv4)) scannedAddresses.add(host + ":5555");
            } catch (Exception ignored) {}
          });
        }
      }
    }
    executor.shutdown();
    try {
      while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {}
    } catch (InterruptedException ignored) {}
    return scannedAddresses;
  }

  // 浏览器打开
  public static void startUrl(Context context,String url) {
    try {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setData(Uri.parse(url));
      context.startActivity(intent);
    } catch (Exception ignored) {
      Toast.makeText(context, context.getString(R.string.error_no_browser), Toast.LENGTH_SHORT).show();
    }
  }

  // 获取解码器是否支持
  public static boolean isDecoderSupport(String mimeName) {
    MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
    for (MediaCodecInfo mediaCodecInfo : mediaCodecList.getCodecInfos()) if (!mediaCodecInfo.isEncoder() && mediaCodecInfo.getName().contains(mimeName)) return true;
    return false;
  }

  // 日志
  public static void logToast(String str) {
    Log.e("Easycontrol", str);
    AppData.uiHandler.post(() -> Toast.makeText(AppData.main, str, Toast.LENGTH_SHORT).show());
  }

  public interface MyFunction {
    void run();
  }

  public interface MyFunctionBoolean {
    void run(Boolean bool);
  }

  public interface MyFunctionButtonBoolean {
    void run(CompoundButton buttonView, Boolean bool);
  }

  public interface MyFunctionString {
    void run(String str);
  }

  public interface MyFunctionInt {
    void run(int num);
  }

}
