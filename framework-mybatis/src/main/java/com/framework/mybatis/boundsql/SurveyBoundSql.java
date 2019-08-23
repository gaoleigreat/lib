package com.framework.mybatis.boundsql;

import com.framework.mybatis.config.SpringContextHolder;
import lombok.Data;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yanglf
 * @description
 * @since 2019/2/25
 **/
@Data
public class SurveyBoundSql extends BoundSql {

    private Configuration configuration;

    private BoundSql boundSql;

    private IProgramCheckService programCheckService;

    private static final Pattern PROGRAM_PATTERN = Pattern.compile("<program.*</program>");


    public SurveyBoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        super(configuration, sql, parameterMappings, parameterObject);
    }

    public SurveyBoundSql(Configuration configuration, BoundSql boundSql) {
        super(configuration, boundSql.getSql(), boundSql.getParameterMappings(), boundSql.getParameterObject());
        this.boundSql = boundSql;
        this.setConfiguration(configuration);
    }

    @Override
    public String getSql() {
        String origin = boundSql.getSql();
        //<program prepend="and" dimension2Field="user:user_id" resourceId="" operationId="" ></program>
        if(origin.indexOf("<program") < 0){
            return origin;
        }


        if(null == programCheckService){
            Object obj = SpringContextHolder.getBean("programCheckService");
            programCheckService = (IProgramCheckService) obj;
        }

        Matcher matcher = PROGRAM_PATTERN.matcher(origin);

        SAXReader reader = new SAXReader();
        StringBuilder sb = new StringBuilder();
        while(matcher.find()){
            sb.delete(0, sb.length());
            String program = matcher.group();
            String condition = "";
            boolean isAll = false;
            try {
                Document document = reader.read(new ByteArrayInputStream(program.getBytes("utf-8")));
                Element element = document.getRootElement();
                String dimension2Field = element.attributeValue("dimension2Field");
                String prepend = element.attributeValue("prepend");
                String resourceId = element.attributeValue("resourceValue");
                String operationId = element.attributeValue("operationValue");
                String[] ary = dimension2Field.split(":");
                sb.append(" ").append(prepend).append(" ");

                Set<String> set = new HashSet<>();
                if(null != programCheckService ){
                    List<String> lst = programCheckService.findProgramItems(ary[0], resourceId, operationId);
                    if(!CollectionUtils.isEmpty(lst)){
                        sb.append(ary[1]).append(" in ( ");
                        for(String s : lst){
                            if("@All".equals(s)){
                                isAll = true;
                                break;
                            }
                            if(set.contains(s)){
                                continue;
                            }
                            set.add(s);
                            sb.append("'").append(s).append("',");
                        }
                        if(sb.length() > 0){
                            sb.delete(sb.length()-1, sb.length());
                        }
                        sb.append(" ) ");
                    }else{
                        sb.append(" 1 = 2 ");
                    }
                }
                if(isAll){
                    condition = " "+ prepend + " 1 = 1 ";
                }else{
                    condition = sb.toString();
                }
            } catch (UnsupportedEncodingException | DocumentException e) {
                e.printStackTrace();
            } finally{
                origin = origin.replaceFirst(program, condition);
            }
        }
        return origin;
    }
}
