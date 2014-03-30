import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FailOverTest {

    final TransportClient client = new TransportClient();

    @Test
    public void testWriting() throws Exception {
        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

        List<String> indexed = new ArrayList<String>(1000000);
        int id = 0;
        while (true) {
            try {
                BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
                for (int x = 0; x < 100; x++) {
                    id++;
                    bulkRequestBuilder.add(client.prepareIndex("i1", "t1", Integer.toString(id)).setSource("field1", "value1"));
                }
                BulkResponse bulkResponse = bulkRequestBuilder.get();
                for (BulkItemResponse item : bulkResponse) {
                    if (!item.isFailed()) {
                        indexed.add(item.getId());
                    }
                }
                System.out.println(String.format("Indexed %d documents", id));
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Total Indexed: " + indexed.size());
        System.out.println("Waiting for ES to become available...");
        while (true) {
            Thread.sleep(5000);
            try {
                ClusterHealthResponse response = client.admin().cluster().prepareHealth().setWaitForGreenStatus().get();
                if (response.getStatus() == ClusterHealthStatus.GREEN) {
                    System.out.println("ES available");
                    break;
                } else {
                    System.out.println("ES Status : " + response.getStatus().name());
                }
            } catch (Exception e) {
                System.out.println("Still not available");
            }
        }

        System.out.println("Checking indexed IDs...");

        int notFound = 0;
        for (String docID : indexed) {
            GetResponse getResponse = client.prepareGet("i1", "t1", docID).get();
            if (!getResponse.isExists()) {
                notFound++;
                System.out.println("Found unresolved ID : " + docID);
            }
        }
        System.out.println("");
        System.out.println("");
        System.out.println("IDs not found : " + notFound);
    }

}