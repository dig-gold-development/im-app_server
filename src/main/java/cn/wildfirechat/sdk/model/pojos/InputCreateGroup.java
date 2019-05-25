/*
 * This file is part of the Wildfire Chat package.
 * (c) Heavyrain2012 <heavyrain.lee@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package cn.wildfirechat.sdk.model.pojos;


import cn.wildfirechat.proto.WFCMessage;
import org.springframework.util.StringUtils;

public class InputCreateGroup extends InputGroupBase {
    private PojoGroup group;

    public boolean isValide() {
        return true;
    }

    public WFCMessage.CreateGroupRequest toProtoGroupRequest() {
        WFCMessage.Group.Builder groupBuilder = WFCMessage.Group.newBuilder();
        WFCMessage.GroupInfo.Builder groupInfoBuilder = WFCMessage.GroupInfo.newBuilder();
        if (!StringUtils.isEmpty(group.getGroup_info().target_id)) {
            groupInfoBuilder.setTargetId(group.getGroup_info().getTarget_id());
        }

        if (!StringUtils.isEmpty(group.getGroup_info().name)) {
            groupInfoBuilder.setName(group.getGroup_info().getName());
        }

        if (!StringUtils.isEmpty(group.getGroup_info().portrait)) {
            groupInfoBuilder.setPortrait(group.getGroup_info().getPortrait());
        }
        if (!StringUtils.isEmpty(group.getGroup_info().owner)) {
            groupInfoBuilder.setOwner(group.getGroup_info().getOwner());
        }

            groupInfoBuilder.setType(group.getGroup_info().getType());

        if (!StringUtils.isEmpty(group.getGroup_info().extra)) {
            groupInfoBuilder.setExtra(group.getGroup_info().getExtra());
        }


        groupBuilder.setGroupInfo(groupInfoBuilder);
        for (PojoGroupMember pojoGroupMember : group.getMembers()) {
            WFCMessage.GroupMember.Builder groupMemberBuilder = WFCMessage.GroupMember.newBuilder().setMemberId(pojoGroupMember.getMember_id());
            if (!StringUtils.isEmpty(pojoGroupMember.getAlias())) {
                groupMemberBuilder.setAlias(pojoGroupMember.getAlias());
            }
            groupMemberBuilder.setType(pojoGroupMember.getType());
            groupBuilder.addMembers(groupMemberBuilder);
        }

        WFCMessage.CreateGroupRequest.Builder createGroupReqBuilder = WFCMessage.CreateGroupRequest.newBuilder();
        createGroupReqBuilder.setGroup(groupBuilder);
        for (Integer line : to_lines
             ) {
            createGroupReqBuilder.addToLine(line);
        }

        createGroupReqBuilder.setNotifyContent(notify_message.toProtoMessageContent());
        return createGroupReqBuilder.build();
    }

    public PojoGroup getGroup() {
        return group;
    }

    public void setGroup(PojoGroup group) {
        this.group = group;
    }
}
