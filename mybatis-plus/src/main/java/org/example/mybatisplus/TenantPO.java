package org.example.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author 猫爸(maoba@dtstack.com)
 * @Date 2017-05-09 1:57 PM
 * @Motto 一生伏首拜阳明
 */
@TableName(value = "uic_tenant", autoResultMap = true)
public class TenantPO extends BasePO {

    /**
     * 租户名称
     */
    @TableField("tenant_name")
    private String tenantName;
    /**
     * 租户描述信息
     */
    @TableField("tenant_desc")
    private String tenantDesc;
    /**
     * 租户联系人姓名(默认为创建租户者)
     */
    @TableField("contact_name")
    private String contactName;
    /**
     * 租户联系邮箱(默认为租户创建者邮箱)
     */
    @TableField("contact_email")
    private String contactEmail;
    /**
     * 租户联系电话(默认为租户创建者电话)
     */
    @TableField("contact_phone")
    private String contactPhone;
    /**
     * 租户所属用户id(当前为创建租户的用户id)
     */
    @TableField("belong_user_id")
    private Long belongUserId;
    /**
     * 与agent交互时的token
     */
    @TableField("agent_token")
    private String agentToken;

    /**
     * 外部id
     */
    @TableField("external_id")
    private String externalId;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantDesc() {
        return tenantDesc;
    }

    public void setTenantDesc(String tenantDesc) {
        this.tenantDesc = tenantDesc;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(Long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getAgentToken() {
        return agentToken;
    }

    public void setAgentToken(String agentToken) {
        this.agentToken = agentToken;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
