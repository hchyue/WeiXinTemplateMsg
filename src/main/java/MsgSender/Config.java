package MsgSender;

/**
 * Created by yuehc on 2020/8/15.
 */
public class Config {
    public String secret;
    public String appid;
    public String templateId;
    public String template;
    /**accessToken重新刷新间隔,以毫秒为单位,默认为1小时 */
    private long accessTokenRefreshInterval;
    /**userList重新刷新间隔,以毫秒为单位,默认为1小时 */
    private long userListRefreshInterval;

    private static Config config;
    public Config()
    {
        accessTokenRefreshInterval=3600000;
        userListRefreshInterval=3600000;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public long getAccessTokenRefreshInterval() {
        return accessTokenRefreshInterval;
    }

    public void setAccessTokenRefreshInterval(long accessTokenRefreshInterval) {
        this.accessTokenRefreshInterval = accessTokenRefreshInterval;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public static Config getInstance() {
        if(config==null)
        {
            config=new Config();
        }
        return config;
    }

    public long getUserListRefreshInterval() {
        return userListRefreshInterval;
    }

    public void setUserListRefreshInterval(long userListRefreshInterval) {
        this.userListRefreshInterval = userListRefreshInterval;
    }
}
