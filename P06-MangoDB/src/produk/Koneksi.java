package produk;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author WIN10
 */
public class Koneksi {
    public static MongoDatabase sambungDB() {
        try {
            MongoClient client = MongoClients.create();
            MongoDatabase database = client.getDatabase("product");
            System.out.println("Koneksi Sukses");
            return database;
        } catch (Exception e) {
            System.out.println("Koneksi Gagal : "+e.getMessage());
        }
        return null;
    }
}
