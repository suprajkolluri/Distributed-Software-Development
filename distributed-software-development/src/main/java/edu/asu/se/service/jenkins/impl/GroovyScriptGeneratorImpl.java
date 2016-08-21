package edu.asu.se.service.jenkins.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import edu.asu.se.service.jenkins.GroovyScriptGenerator;

@Service
@PropertySource("classpath:application-${env}.properties")
public class GroovyScriptGeneratorImpl implements GroovyScriptGenerator {

	@Autowired
	Environment env;

	@Override
	public Boolean generateScipt(String projectName, String rootPOMLoc) {

		String filePath = env.getProperty("DSLJobWSPath") + "/DSL Git projects builder/" + projectName.replaceAll("/", "-") + ".groovy";

		StringBuffer buf = new StringBuffer();
		buf.append("def project = '").append(projectName).append("'\n");
		buf.append("def branchApi = new URL(\"https://api.github.com/repos/${project}/branches\")").append("\n");
		buf.append("def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())").append("\n");
		buf.append("branches.each {").append("\n");
		buf.append("\t").append("def branchName = it.name").append("\n");
		buf.append("\t").append("def jobName = \"${project}-${branchName}\".replaceAll('/','-')").append("\n");
		buf.append("\t").append("mavenJob(jobName){").append("\n");
		buf.append("\t\t").append("logRotator(-1, 3)").append("\n");
		buf.append("\t\t").append("scm {").append("\n");
		buf.append("\t\t\t").append("github(project, '*/' + branchName)").append("\n");
		buf.append("\t\t").append("}").append("\n");
		buf.append("\t\t").append("triggers {").append("\n");
		buf.append("\t\t\t").append("scm('* * * * *')").append("\n");
		buf.append("\t\t").append("}").append("\n");
		buf.append("\t\t").append("rootPOM('").append(rootPOMLoc).append("')").append("\n");
		buf.append("\t\t").append("goals('clean test')").append("\n");
		buf.append("\t").append("}").append("\n");
		buf.append("}");

		Boolean result = null;
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
			writer.write(buf.toString());
			result = true;
		} catch (IOException ex) {
			result = false;
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				result = false;
			}
		}

		return result;
	}

}
