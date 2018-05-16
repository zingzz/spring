package com.ssm.generator.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MybatisGeneratorUtil {

	public static void main(String[] args) throws Exception {
		generator();
	}

	private static void generator() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File(MybatisGeneratorUtil.class.getClassLoader().getResource("generatorConfig.xml").getFile());
		ConfigurationParser cp = new ConfigurationParser(warnings);

		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		

		System.out.println("Éú³ÉÍê±Ï");
	}
}