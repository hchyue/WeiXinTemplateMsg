package MsgSender;

/**
 * Created by yuehc on 2020/8/15.
 */
public class Test {
    public static void main(String args[])
    {
        Config config=Config.getInstance();
        config.setAppid("wxaa520c9978d0b71c");
        config.setSecret("63820aef1817ce9f32dd60e9dc0966dc");
        config.setTemplateId("8ERKVMdN8pOuqBCooFQ0pcP8VJ3gl0AKZagRThW0UGo");
        config.setTemplate("{{first.DATA}}\n" +
                "告警时间：{{keyword1.DATA}}\n" +
                "所属应用：{{keyword2.DATA}}\n" +
                "告警级别：{{keyword3.DATA}}\n" +
                "设备名称：{{keyword4.DATA}}\n" +
                "告警描述：{{keyword6.DATA}}\n" +
                "{{remark.DATA}}");
        config.setAccessTokenRefreshInterval(3600000);

        TemplateMsgSender msgSender=new TemplateMsgSender(config);
        String alarm="{{first.DATA}}\n" +
                "告警时间：{{keyword1.DATA}}\n" +
                "所属应用：{{keyword2.DATA}}\n" +
                "告警级别：{{keyword3.DATA}}\n" +
                "设备名称：{{keyword4.DATA}}\n" +
                "告警描述：{{keyword6.DATA}}\n" +
                "{{remark.DATA}}";
        msgSender.send(alarm);
    }
}
