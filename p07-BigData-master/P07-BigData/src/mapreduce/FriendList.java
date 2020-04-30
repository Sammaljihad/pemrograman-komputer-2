package mapreduce;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Map;
import org.bson.Document;

public class FriendList {

    public static void main(String[] args) {
        try {
            MongoDatabase db = Koneksi.sambungDB();
            MongoCollection<Document> collection = db.getCollection("friendlist");
            FindIterable<Document> friendList = collection.find();
            ArrayList<Document> friends = new ArrayList<>();
            for (Document friend : friendList) {
                friends.add(friend);
            }
            ArrayList<Document> mapResult = MapReduce.map(friends);
            Map<ArrayList, ArrayList<ArrayList>> groupResult = MapReduce.group(mapResult);
            Map<ArrayList, ArrayList> reduceResult = MapReduce.reduce(groupResult);

            System.out.println("------Friend List------------------");
            friends.forEach((f) -> {
                String name = f.getString("name");
                ArrayList<String> theFriends = (ArrayList) f.get("friends");
                System.out.println("[" + name + "] berteman dengan: ");
                for (int i = 0; i < theFriends.size(); i++) {
                    System.out.println("\t" + (i + 1) + ". " + theFriends.get(i));
                }
            });
            System.out.println("\n\n");

            System.out.println("------Map Result------------------");
            mapResult.forEach((k) -> {
                ArrayList names = (ArrayList) k.get("key");
                ArrayList theFriends = (ArrayList) k.get("value");
                System.out.print("Teman dari " + names.toString() + "\n");
                for (int i = 0; i < theFriends.size(); i++) {
                    System.out.println("\t" + (i + 1) + ". " + theFriends.get(i));
                }
            });
            System.out.println("\n\n");

            System.out.println("------Group Result------------------");
            groupResult.entrySet().forEach((k) -> {
                ArrayList names = (ArrayList) k.getKey();
                ArrayList<ArrayList> group = (ArrayList) k.getValue();
                System.out.println("Group pertemanan dari " + names.toString());
                int n = 1;
                for (ArrayList g : group) {
                    System.out.println("\tGroup " + n);
                    int ls = 1;
                    for (Object gm : g) {
                        System.out.println("\t" + ls + ". " + gm);
                        ls++;
                    }
                    n++;
                }
            });
            System.out.println("\n\n");

            System.out.println("------Reduce Result------------------");
            reduceResult.entrySet().forEach((k) -> {
                ArrayList names = (ArrayList) k.getKey();
                ArrayList fic = (ArrayList) k.getValue();
                String status;
                if (fic.size() > 0) {
                    status = " memiliki teman yang sama: \n";
                } else {
                    status = " TIDAK memiliki teman yang sama\n";
                }
                System.out.print(names.get(0) + " dan " + names.get(1) + status);
                for (int i = 0; i < fic.size(); i++) {
                    System.out.println("\t" + (i + 1) + ". " + fic.get(i));
                }
            });

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
