package com.han.messaging.dao;

import com.han.messaging.model.FriendInvitation;
import com.han.messaging.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendDAO {

    @Insert("INSERT INTO friend_invitation (sender_user_id, receiver_user_id, send_time, message, status) " +
            "VALUES (#{senderUserId}, #{receiverUserId}, #{sendTime}, #{message}, #{status}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(FriendInvitation friendInvitation);

    @Select("select * from friend_invitation where receiver_user_id = #{receiverUserId} order by send_time desc")
    List<FriendInvitation> showInvitation(@Param("receiverUserId") Integer receiverUserId);


    @Select("select * from friend_invitation where id = #{invitationId}")
    FriendInvitation selectByInvitationId(@Param("invitationId") Integer invitationId);

    @Select("select * from friend_invitation where receiver_user_id = #{receiverId} and sender_user_id = #{senderId}")
    FriendInvitation checkExisted(@Param("receiverId") Integer receiverId, @Param("senderId") Integer senderId);

    @Update("update friend_invitation set status = 'ACCEPTED' where id = #{invitationId}")
    void acceptById(@Param("invitationId") Integer invitationId);

    @Update("update friend_invitation set status = 'REJECTED' where id = #{invitationId}")
    void rejectById(@Param("invitationId") Integer invitationId);

    @Select("select * from friend_invitation where receiver_user_id = #{receiverId} and status = 'ACCEPTED'")
    List<FriendInvitation> findFriends(@Param("receiverId") Integer receiverId);
}
