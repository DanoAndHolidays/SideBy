package com.sideby.system.service;

import com.sideby.common.exception.DatabaseUnavailableException;
import com.sideby.system.mapper.SystemStatusMapper;
import com.sideby.system.vo.SystemStatusVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SystemStatusService {

    private static final Logger log = LoggerFactory.getLogger(SystemStatusService.class);
    private final SystemStatusMapper systemStatusMapper;

    public SystemStatusService(SystemStatusMapper systemStatusMapper) {
        this.systemStatusMapper = systemStatusMapper;
    }

    public SystemStatusVO getStatus() {
        try {
            if (systemStatusMapper.checkDatabase() != 1) {
                throw new DatabaseUnavailableException();
            }
            return new SystemStatusVO("SideBy", "UP", "UP");
        } catch (DatabaseUnavailableException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.warn("Database health query failed: {}", exception.getMessage());
            throw new DatabaseUnavailableException();
        }
    }
}

