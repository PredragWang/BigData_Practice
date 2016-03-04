package gw.mapredproj.mappers;
/**
 * Created by Guanyu on 3/4/16.
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FriendRecMapper
        extends Mapper <Object, Text, Text, Text>{
    private Text user = new Text();
    private Text friendValue = new Text();
    @Override
    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
        String line = value.toString();
        if (line == null || line.length() == 0) return;
        String[] userAndFriends = line.split("\t");
        if (userAndFriends.length < 2) return;
        String[] friends = userAndFriends[1].split(",");
        if (friends.length < 2) return;
        for (int i = 0; i < friends.length; ++i) {
            user.set(userAndFriends[0]);
            friendValue.set("1," + friends[i]);
            context.write(user, friendValue);
            user.set(friends[i]);
            friendValue.set("1,"+userAndFriends[0]);
            for (int j = i + 1; j < friends.length; ++ j) {
                user.set(friends[i]);
                friendValue.set("2," + friends[j] + "," + userAndFriends[0]);
                context.write(user, friendValue);
            }
        }
    }
}
