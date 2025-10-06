package com.example.realtimechatapp.repository;

import com.example.realtimechatapp.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // When spring sees @Query, it creates a proxy class that knows how to execute that query.
    // spring builds the SQL query from your JPQL, executes it on the database,
    //converts each result row into a ChatMessage object, returns a List<ChatMessage>.
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.type = 'PRIVATE_MESSAGE' AND " +
            "((cm.sender = :user1 AND cm.recipient = :user2) OR (cm.sender = :user2 AND cm.recipient = :user1)) " +
            "ORDER BY cm.timeStamp ASC")
    // @Param() links method parameters to query placeholders (:user1, :user2)
    List<ChatMessage> findPrivateMessagesBetweenTwoUsers(@Param("user1") String user1, @Param("user2") String user2);
}
