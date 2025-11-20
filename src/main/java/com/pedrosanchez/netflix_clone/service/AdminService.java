package com.pedrosanchez.netflix_clone.service;

import org.springframework.core.io.Resource;
import java.io.IOException;

public interface AdminService {
    Resource createBackup(String filePath) throws IOException;
}
