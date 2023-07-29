package com.scaling.libraryservice.commons.api.service;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanableLibProviderTest {

    @Autowired
    private LoanableLibProvider loanableLibProvider;

    @BeforeEach
    public void setUp() {
    }

    public void provideDataList() {
        /* given */

        ApiConnection bExistConn1 = new LoanableLibConn(141099, "9788965700036");
        ApiConnection bExistConn2 = new LoanableLibConn(111036, "9788965700036");

        List<ApiConnection> connections = List.of(bExistConn1, bExistConn2);

        /* when */

        var result = loanableLibProvider.provideDataList(connections, 3);

        /* then */
        assertEquals(2, result.size());
        assertTrue(result.stream()
            .anyMatch(r -> r.getIsbn13().equals("9788965700036")));

    }

}