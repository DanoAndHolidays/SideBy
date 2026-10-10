package com.sideby.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.sideby.common.exception.DatabaseUnavailableException;
import com.sideby.system.mapper.SystemStatusMapper;
import org.junit.jupiter.api.Test;

class SystemStatusServiceTest {

    @Test
    void reportsUpAfterRealDatabaseProbeSucceeds() {
        SystemStatusMapper mapper = () -> 1;
        SystemStatusService service = new SystemStatusService(mapper);

        assertThat(service.getStatus().database()).isEqualTo("UP");
    }

    @Test
    void convertsDatabaseFailureToSafeBusinessException() {
        SystemStatusMapper mapper = () -> {
            throw new RuntimeException("connection details");
        };
        SystemStatusService service = new SystemStatusService(mapper);

        assertThatThrownBy(service::getStatus)
                .isInstanceOf(DatabaseUnavailableException.class)
                .hasMessage("Database is currently unavailable");
    }
}
