package com.example.controller.stream;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping("/demo")
public class ControllerTest {


    @Autowired
    DataSource dataSource;

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
