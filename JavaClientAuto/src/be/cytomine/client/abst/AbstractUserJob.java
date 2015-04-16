
package be.cytomine.client.abst;

import be.cytomine.client.*;
import java.util.*;
import org.json.simple.*;

import java.util.Date;
import be.cytomine.client.Server;
import be.cytomine.client.UserJob;
import org.json.simple.JSONObject;


/**
 * A cytomine software user
 * 
 * @author ClientBuilder (Loïc Rollus)
 * @version 0.1
 * 
 * DO NOT EDIT THIS FILE. THIS IS CODE IS BUILD AUTOMATICALY. ALL CHANGE WILL BE LOST AFTER NEXT GENERATION.
 * 
 * IF YOU WANT TO EDIT A DOMAIN FILE (change method, add property,...), JUST EDIT THE CHILD FILE “YourDomain.java” instead of this file “AbstractYourDomain.java”. I WON'T BE CLEAR IF IT ALREADY EXIST.
 * 
 */
public abstract class AbstractUserJob
    extends AbstractDomain
{

    /**
     * The full class name of the domain
     * 
     */
    protected String clazz;
    /**
     * The domain id
     * 
     */
    protected Long id;
    /**
     * The date of the domain creation
     * 
     */
    protected Date created;
    /**
     * The date of the domain modification
     * 
     */
    protected Date updated;
    /**
     * When domain was removed from Cytomine
     * 
     */
    protected Date deleted;
    /**
     * The username of the user
     * 
     */
    protected String username;
    /**
     * If true, user is a userjob
     * 
     */
    protected Boolean algo;
    /**
     * The username of the user that launch this job
     * 
     */
    protected String humanUsername;
    /**
     * The user public key
     * 
     */
    protected String publicKey;
    /**
     * The user private key
     * 
     */
    protected String privateKey;
    /**
     * The related job
     * 
     */
    protected Long job;
    /**
     * Human user that launch the job
     * 
     */
    protected Long user;
    /**
     * The rate succes of the job
     * 
     */
    protected Double rate;
    /**
     * If true, account is expired
     * 
     */
    protected Boolean accountExpired;
    /**
     * If true, account is locked
     * 
     */
    protected Boolean accountLocked;
    /**
     * If true, account is enabled
     * 
     */
    protected Boolean enabled;
    /**
     * The user password
     * 
     */
    protected String password;
    /**
     * If true, password is expired
     * 
     */
    protected Boolean passwordExpired;

    /**
     * 
     * @return
     *     The full class name of the domain
     */
    public String getClazz()
        throws Exception
    {
        return clazz;
    }

    /**
     * 
     * @return
     *     The domain id
     */
    public Long getId()
        throws Exception
    {
        return id;
    }

    /**
     * 
     * @return
     *     The date of the domain creation
     */
    public Date getCreated()
        throws Exception
    {
        return created;
    }

    /**
     * 
     * @return
     *     The date of the domain modification
     */
    public Date getUpdated()
        throws Exception
    {
        return updated;
    }

    /**
     * 
     * @return
     *     When domain was removed from Cytomine
     */
    public Date getDeleted()
        throws Exception
    {
        return deleted;
    }

    /**
     * 
     * @return
     *     The username of the user
     */
    public String getUsername()
        throws Exception
    {
        return username;
    }

    /**
     * 
     * @param username
     *     The username of the user
     */
    public void setUsername(String username)
        throws Exception
    {
        this.username = username;
    }

    /**
     * 
     * @return
     *     If true, user is a userjob
     */
    public Boolean getAlgo()
        throws Exception
    {
        return algo;
    }

    /**
     * 
     * @return
     *     The username of the user that launch this job
     */
    public String getHumanUsername()
        throws Exception
    {
        return humanUsername;
    }

    /**
     * 
     * @return
     *     The user public key
     */
    public String getPublicKey()
        throws Exception
    {
        return publicKey;
    }

    /**
     * 
     * @param publicKey
     *     The user public key
     */
    public void setPublicKey(String publicKey)
        throws Exception
    {
        this.publicKey = publicKey;
    }

    /**
     * 
     * @return
     *     The user private key
     */
    public String getPrivateKey()
        throws Exception
    {
        return privateKey;
    }

    /**
     * 
     * @param privateKey
     *     The user private key
     */
    public void setPrivateKey(String privateKey)
        throws Exception
    {
        this.privateKey = privateKey;
    }

    /**
     * 
     * @return
     *     The related job
     */
    public Long getJob()
        throws Exception
    {
        return job;
    }

    /**
     * 
     * @param job
     *     The related job
     */
    public void setJob(Long job)
        throws Exception
    {
        this.job = job;
    }

    /**
     * 
     * @return
     *     Human user that launch the job
     */
    public Long getUser()
        throws Exception
    {
        return user;
    }

    /**
     * 
     * @param user
     *     Human user that launch the job
     */
    public void setUser(Long user)
        throws Exception
    {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The rate succes of the job
     */
    public Double getRate()
        throws Exception
    {
        return rate;
    }

    /**
     * 
     * @param password
     *     The user password
     */
    public void setPassword(String password)
        throws Exception
    {
        this.password = password;
    }

    public void build(String username, Long job, Long user, String password)
        throws Exception
    {
        
		this.username=username;
		this.job=job;
		this.user=user;
		this.password=password;

    
    }

    public void build(JSONObject json)
        throws Exception
    {
        
		this.clazz =JSONUtils.extractJSONString(json.get("class"));
		this.id =JSONUtils.extractJSONLong(json.get("id"));
		this.created =JSONUtils.extractJSONDate(json.get("created"));
		this.updated =JSONUtils.extractJSONDate(json.get("updated"));
		this.deleted =JSONUtils.extractJSONDate(json.get("deleted"));
		this.username =JSONUtils.extractJSONString(json.get("username"));
		this.algo =JSONUtils.extractJSONBoolean(json.get("algo"));
		this.humanUsername =JSONUtils.extractJSONString(json.get("humanUsername"));
		this.publicKey =JSONUtils.extractJSONString(json.get("publicKey"));
		this.privateKey =JSONUtils.extractJSONString(json.get("privateKey"));
		this.job =JSONUtils.extractJSONLong(json.get("job"));
		this.user =JSONUtils.extractJSONLong(json.get("user"));
		this.rate =JSONUtils.extractJSONDouble(json.get("rate"));
		this.accountExpired =JSONUtils.extractJSONBoolean(json.get("accountExpired"));
		this.accountLocked =JSONUtils.extractJSONBoolean(json.get("accountLocked"));
		this.enabled =JSONUtils.extractJSONBoolean(json.get("enabled"));
		this.password =JSONUtils.extractJSONString(json.get("password"));
		this.passwordExpired =JSONUtils.extractJSONBoolean(json.get("passwordExpired"));

    
    }

    public JSONObject toJSON()
        throws Exception
    {
        
		JSONObject json=new JSONObject();
		json.put("class",JSONUtils.formatJSON(this.clazz));
		json.put("id",JSONUtils.formatJSON(this.id));
		json.put("created",JSONUtils.formatJSON(this.created));
		json.put("updated",JSONUtils.formatJSON(this.updated));
		json.put("deleted",JSONUtils.formatJSON(this.deleted));
		json.put("username",JSONUtils.formatJSON(this.username));
		json.put("algo",JSONUtils.formatJSON(this.algo));
		json.put("humanUsername",JSONUtils.formatJSON(this.humanUsername));
		json.put("publicKey",JSONUtils.formatJSON(this.publicKey));
		json.put("privateKey",JSONUtils.formatJSON(this.privateKey));
		json.put("job",JSONUtils.formatJSON(this.job));
		json.put("user",JSONUtils.formatJSON(this.user));
		json.put("rate",JSONUtils.formatJSON(this.rate));

		return json;
    
    }

    public static UserJob showUserJob(Server server, Long id, Integer max, Integer offset)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

    public static Object listUserJobByProject(Server server, Long id, Boolean tree, Long image, Integer max, Integer offset)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

    public static Object createUserJob(Server server, Long JSON_POST_DATA__parent, Long JSON_POST_DATA__job, Long JSON_POST_DATA__software, Long JSON_POST_DATA__project)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

}
