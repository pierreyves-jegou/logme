package org.caillou.company.util;

import org.caillou.company.constant.Level;
import org.slf4j.Logger;

public interface LevelExtractorService {

    Level extractCurrentLevel(Logger logger);

}
