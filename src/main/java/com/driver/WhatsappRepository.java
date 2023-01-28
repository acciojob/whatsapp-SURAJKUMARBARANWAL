package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;


    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;

    }
    public String createUser(String  name,String mobileNum){
        if(userMobile.contains(mobileNum)){
            return "mobile is register, already";
        }
        userMobile.add(mobileNum);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        int n= users.size();
        if(n==2){
            //private chat
            Group group=new Group(users.get(1).getName(),n);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            groupMessageMap.put(group,new ArrayList<>());
            return group;
        }
        else if(n>2){
            //group is more than 2 members
            Group group=new Group( "Group "+(++customGroupCount),n);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            groupMessageMap.put(group,new ArrayList<>());
            return group;
        }
        return null;
    }

    public int createMessage(String content) {
        Message msg=new Message(++messageId,content);
        msg.setId(++messageId);
        msg.setTimestamp(new Date());
        return msg.getId();
    }


    public int sendMessage(Message message, User sender, Group group) {
      if(!groupUserMap.containsKey(group)){
          //-1 group does not exist
          return -1;
      }
      else if(!groupUserMap.get(group).contains(sender)){
          //user is not member in that group
          return -2;
      }
      else{
          //this sender is in the group
          senderMap.put(message,sender);
          groupMessageMap.get(group).add(message);
          return groupMessageMap.get(group).size();
      }
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!groupUserMap.containsKey(group)){
            return  "group not find";
        }
        else if(adminMap.get(group).equals(approver)==false){
            return "Approver is not admin";
        }
        else if(groupUserMap.get(group).contains(user)==false){
            return "User not found in group";
        }
        else {
            //now change
            adminMap.put(group,user);
            return "SUCCESS";
        }
    }
}
