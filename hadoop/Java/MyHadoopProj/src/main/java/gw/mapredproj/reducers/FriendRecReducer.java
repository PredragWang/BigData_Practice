package gw.mapredproj.reducers;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by Guanyu on 3/4/16.
 */
public class FriendRecReducer
        extends Reducer <Text, Text, Text, Text> {

    public static class RecommendedFriend {
        private String friendId;
        private ArrayList<String> commonFriends;
        public RecommendedFriend(String friendId, ArrayList<String>commenFriends) {
            this.friendId = friendId;
            this.commonFriends = commenFriends;
        }
        public String getFriendId() {
            return this.friendId;
        }
        public int countCommonFriends() {
            return this.commonFriends.size();
        }
        public String getCommonFriendsStr() {
            StringBuilder sb = new StringBuilder();
            for (String f : commonFriends) {
                sb.append((sb.length() == 0 ? f : ("," + f)));
            }
            return sb.toString();
        }
    }

    private Text user;
    private String[] valueSplit;
    private ArrayList<String> friends;
    private HashMap<String, ArrayList<String>> recommendedFriendsMap;
    private ArrayList<RecommendedFriend> recommendedFriends;

    private Text outputRecommendedFriends () {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (RecommendedFriend rf : recommendedFriends) {
            if (sb.length() > 0) sb.append(';');
            sb.append(rf.getFriendId() + "(");
            sb.append(rf.countCommonFriends());
            sb.append(":" + rf.getCommonFriendsStr() + ")");
            if (++count >= 10) break;
        }
        return new Text(sb.toString());
    }
    public void reduce(Text key, Iterable<Text> values,
                       Context context) throws IOException, InterruptedException {
        user = key;
        // Store the friends of a user and recommended friends of the user
        friends = new ArrayList<String>();
        recommendedFriendsMap = new HashMap<String, ArrayList<String>>();
        recommendedFriends = new ArrayList<RecommendedFriend>();
        for (Text friendEntry : values) {
            valueSplit = friendEntry.toString().split(",");
            if (valueSplit[0].equals("1")) {
                friends.add(valueSplit[1]);
            }
            else {
                ArrayList<String> commonFriends = recommendedFriendsMap.get(valueSplit[1]);
                if (commonFriends == null) {
                    commonFriends = new ArrayList<String>();
                    commonFriends.add(valueSplit[2]);
                    recommendedFriendsMap.put(valueSplit[1], commonFriends);
                }
                else {
                    commonFriends.add(valueSplit[2]);
                }
            }
        }
        // Remove the existing friends from recommended friends
        for (String f : friends) {
            recommendedFriendsMap.remove(f);
        }

        // Store the recommended friends into the list and sort
        for (Map.Entry<String, ArrayList<String>> entry : recommendedFriendsMap.entrySet()) {
            recommendedFriends.add(new RecommendedFriend(entry.getKey(), entry.getValue()));
        }
        Collections.sort(recommendedFriends, new Comparator<RecommendedFriend>() {
            public int compare(RecommendedFriend o1, RecommendedFriend o2) {
                return o2.countCommonFriends() - o1.countCommonFriends();
            }
        });

        //Write the result
        try {
            context.write(user, outputRecommendedFriends());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
