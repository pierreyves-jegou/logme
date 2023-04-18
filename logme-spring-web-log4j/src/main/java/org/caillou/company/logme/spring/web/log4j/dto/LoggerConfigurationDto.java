package org.caillou.company.logme.spring.web.log4j.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoggerConfigurationDto {

    private String loggerKey;

    private String loggerLevel;

}
