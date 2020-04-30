package mapreduce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;

public class MapReduce {

    public static ArrayList<Document> map(ArrayList<Document> friendlist) {
        ArrayList<Document> mapResult = new ArrayList<>();
        friendlist.stream().map((personFriend) -> {
            ArrayList<Document> mapFriend = new ArrayList<>();
            String name = personFriend.getString("name");
            ArrayList friends = (ArrayList) personFriend.get("friends");
            for (int i = 0; i < friends.size(); i++) {
                ArrayList key = new ArrayList();
                key.add(name);
                key.add(friends.get(i));
                Document kv = new Document();
                kv.append("key", key);
                kv.append("value", friends);
                Collections.sort(key);
                mapFriend.add(kv);
            }
            return mapFriend;
        }).forEachOrdered((mapFriend) -> {
            mapResult.addAll(mapFriend);
        });
        return mapResult;
    }

    public static Map<ArrayList, ArrayList<ArrayList>> group(ArrayList<Document> mapResult) {
        Map<ArrayList, ArrayList<ArrayList>> groupResult = new HashMap<>();

        mapResult.forEach((kv) -> {
            groupResult.put((ArrayList) kv.get("key"), new ArrayList<>());
        });

        mapResult.forEach((k) -> {
            groupResult.get((ArrayList) k.get("key")).add((ArrayList) k.get("value"));
        });
        return groupResult;
    }

    public static ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> result = new ArrayList<>(list1);
        result.retainAll(list2);
        return result;
    }

    public static Map<ArrayList, ArrayList> reduce(Map<ArrayList, ArrayList<ArrayList>> groupResult) {
        Map<ArrayList, ArrayList> reduceResult = new HashMap<>();

        groupResult.entrySet().forEach((kv) -> {
            ArrayList key = (ArrayList) kv.getKey();
            ArrayList<ArrayList> value = (ArrayList) kv.getValue();

            ArrayList<String> result = value.get(0);
            for (int i = 1; i < value.size(); i++) {
                result = intersection(value.get(i), result);
            }
            reduceResult.put(key, result);
        });
        return reduceResult;
    }
}