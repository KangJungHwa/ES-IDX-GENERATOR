package com.dd.elk;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Slf4j
@Component
public class ElkIndexRunner implements ApplicationRunner {

    @Autowired
    @Qualifier("client")
    RestHighLevelClient client;

    @Autowired
    @Qualifier("conn")
    Connection conn;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long startMillis = System.currentTimeMillis();

        String table_name=args.getOptionValues("table_name").get(0);
        log.info("table_name 파라메터는  {} 입니다.", table_name);
        genElkIndex(table_name);
        String endMillis = String.valueOf(System.currentTimeMillis() - startMillis);
        log.info("총 처리 시간은 {}ms 입니다.", endMillis);
        System.exit(0);
    }

    /**
     * result Set 생성 메소드
     * @param conn
     * @param stmt
     * @param rs
     * @param table_name
     * @return
     */
    private ResultSet getJobResultSet(Connection conn,Statement stmt, ResultSet rs,String table_name) {
        try {
            String query = null;
            if (table_name.equals("cat_tab_mas")){
                query = "select table_id,table_nm,table_desc, table_tag from cat_tab_mas";
            }else{
                query = "select word_id, synonym from cat_std_word";
            }
//            conn = getConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            log.error(e.toString());
        }

        return rs;
    }

    /**
     * index document generator
     * @param rs
     * @param table_name
     * @throws SQLException
     * @throws IOException
     */
    public void insertCatTabMasDocuments(ResultSet rs, String table_name) throws SQLException, IOException {

        BulkRequest bulkRequest = new BulkRequest();
        int index=0;
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest("cat_tab_mas");
            request.setQuery(QueryBuilders.matchAllQuery());

            client.deleteByQuery(request, RequestOptions.DEFAULT);
            while (rs.next()) {
                int table_id = rs.getInt("table_id");
                String table_nm = rs.getString("table_nm");
                String table_desc = rs.getString("table_desc");
                String table_tag = rs.getString("table_tag");
                log.info("table_id : " + table_id);
                log.info("table_nm : " + table_nm);
                log.info("table_desc : " + table_desc);
                log.info("table_tag : " + table_tag);
                bulkRequest.add(
                        new IndexRequest("cat_tab_mas")
                                .source(XContentType.JSON,
                                        "table_id", table_id,
                                        "table_nm", table_nm,
                                        "table_desc", table_desc,
                                        "table_tag", table_tag));
                index++;
                if (index % 2000 == 0) {
                    BulkResponse res = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                    bulkRequest = new BulkRequest();
                }
            }
            BulkResponse res = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("res.getTook() : " + res.getTook());
        } catch (Exception e) {
            log.error("ERROR :",e);
        }finally {
            client.close();
        }
    }


    /**
     * 최초 호출되는 메소드
     * @param table_name
     */
    private void genElkIndex(String table_name){

        Statement stmt=null;
        ResultSet rs=null;

        try {

            rs = getJobResultSet(conn, stmt, rs, table_name);
            if (table_name.equals("cat_tab_mas")) {
                insertCatTabMasDocuments(rs, table_name);
            }else{
                insertCatTabMasDocuments(rs, table_name);
            }

        } catch (SQLException e) {
            log.info(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try{conn.close();}catch (Exception e){log.error(e.toString());}
            if (stmt != null) try{stmt.close();}catch (Exception e){log.error(e.toString());}
            if (rs != null) try{rs.close();}catch (Exception e){log.error(e.toString());}
        }
    }
}
