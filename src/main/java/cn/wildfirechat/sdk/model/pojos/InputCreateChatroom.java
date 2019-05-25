/*
 * This file is part of the Wildfire Chat package.
 * (c) Heavyrain2012 <heavyrain.lee@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package cn.wildfirechat.sdk.model.pojos;


import cn.wildfirechat.proto.ProtoConstants;
import cn.wildfirechat.proto.WFCMessage;
import org.springframework.util.StringUtils;

public class InputCreateChatroom {
    private String chatroomId;
    private String title;
    private String desc;
    private String portrait;
    private String extra;
    private Integer state;

    public WFCMessage.ChatroomInfo toChatroomInfo() {
        WFCMessage.ChatroomInfo.Builder builder = WFCMessage.ChatroomInfo.newBuilder().setTitle(title);
        if (!StringUtils.isEmpty(desc)) {
            builder.setDesc(desc);
        }

        if (!StringUtils.isEmpty(portrait)) {
            builder.setPortrait(portrait);
        }

        long current = System.currentTimeMillis();
        builder.setCreateDt(current).setUpdateDt(current).setMemberCount(0);

        if (!StringUtils.isEmpty(extra)) {
            builder.setExtra(extra);
        }

        if (state == null || state == 0) {
            builder.setState(ProtoConstants.ChatroomState.Chatroom_State_Normal);
        } else if(state == 1) {
            builder.setState(ProtoConstants.ChatroomState.Chatroom_State_NotStart);
        } else {
            builder.setState(ProtoConstants.ChatroomState.Chatroom_State_End);
        }

        return builder.build();
    }
    public InputCreateChatroom() {
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
