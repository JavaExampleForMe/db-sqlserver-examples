package com.example.controller.stream;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@RestController
@RequestMapping("/demo")
public class ControllerTest {

    @Autowired
    DataSource dataSource;

    @RequestMapping("/ioConsumer")
    public void ioConsumer() {
         RestTemplate restTemplate = new RestTemplate();

        ResponseExtractor<Resource> responseExtractor = response -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("end....");
            return null;
        };

        RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

        restTemplate.execute(URI.create("http://localhost:8888/demo/ioFromDB"), HttpMethod.GET, requestCallback, responseExtractor);
    }

    @RequestMapping(method = RequestMethod.GET, path = "ioFromDB")
    public StreamingResponseBody stream() throws FileNotFoundException, SQLException {
        return getQueryResult();
    }



    public StreamingResponseBody getQueryResult() throws FileNotFoundException, SQLException {
        PreparedStatement sqlCommand = dataSource.getConnection().prepareStatement("SELECT * FROM  [TestStreaming] ORDER BY Id");
        ResultSet result = sqlCommand.executeQuery();
        return (out) -> {
            try {
                while (result.next()) {
                    Integer id = result.getInt("Id");
                    out.write(("Id = " + id + "\n").getBytes());
                    System.out.println(id);
                    out.flush();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }

//------------------------------------------------- Another example ------------------------------------------
    // http://localhost:8888/demo/test
    @RequestMapping("/test")
    public StreamingResponseBody handleRequestTest(HttpServletResponse response) {
        return new ControllerTest.StreamingCsv();
    }

    public class StreamingCsv implements StreamingResponseBody {
        @Override
        @SneakyThrows
        public void writeTo(OutputStream out) throws IOException {
            String sql = "SELECT * FROM [TestStreaming]";

            try (java.sql.Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    out.write(("Id = " + result.getInt("Id") + "\n").getBytes());
                    out.flush();
                }
            } catch (Exception e) {
                System.out.println("test");
            }
        }
    }


}
