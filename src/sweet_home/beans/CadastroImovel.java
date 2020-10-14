package sweet_home.beans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.primefaces.event.SelectEvent;

import sweet_home.Endereco;
import sweet_home.Imovel;
import sweet_home.Tipo;
import sweet_home.Usuario;
import sweet_home.servico.EnderecoServico;
import sweet_home.servico.ImovelServico;
import sweet_home.servico.UsuarioServico;

import org.apache.commons.io.IOUtils;



@ManagedBean
@javax.faces.bean.SessionScoped
public class CadastroImovel implements Serializable {
      
	private static final long serialVersionUID = 1L;
	
	@EJB
    private ImovelServico imovelServico; 
	@EJB
    private UsuarioServico usuarioServico; 
	
    private String banheiros;    
    private String quartos;
    private String salas;
    private static Usuario usuario = null;
    private String tipo = "1";
    private String descricao = null;
    private String valor;
    private boolean piscina;
    private boolean garagem;
    private boolean salaReuniao;
    private boolean beiraMar;
    private int filtrar = 0;
    private UIComponent mybutton;
    protected static String resp = "";
        
    private String cidade = null;
    private String bairro = null;
    private String rua = null;
    private String numero = null;    
    private String CEP = null;
    private String estado = null;
    private static List<Imovel> lista = null;
    private static Imovel imovel = null;
    
    
    
        
    private Part imageFile; 
    
    
    public String cadastrar() {
                
        lista = imovelServico.recuperarImoveis();
                        
        boolean existe = imovelServico.existe(imovel);
        
		Endereco endereco = new Endereco(null, rua, numero, bairro, cidade, estado, CEP, null);

        
        if(!existe) {
        	imovel = new Imovel();
        	
            imovel.setEndereco(endereco);
        }               
       
                
        HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("logado");
                
        imovel.setQuartos(Integer.parseInt(quartos));
        imovel.setBanheiros(Integer.parseInt(banheiros));
        imovel.setSalas(Integer.parseInt(salas));
        imovel.setTipo(Integer.parseInt(tipo));
        imovel.setDescricao(descricao);
        //imovel.setValor(par);
        
        imovel.setUsuario(usuarioLogado);
        
        imovelServico.persistir(imovel);    
        resp = "Imóvel cadastrado com sucesso!";
        
        return "cadastro";
    }
      
    
    
     
    public void excluir(String s) {              
    	
       Imovel i = imovelServico.consultarPorId(new Long(s));    	
       imovelServico.remover(i);      
    }

    
    public String salvarImagem() {
    	
    	try {
	    	InputStream is = imageFile.getInputStream();
	    	byte[] bytes = IOUtils.toByteArray(is);
	    	
	    	return "";
		    	
    	}catch(IOException e) {}
    	
    	return "";
    }
    
    public Part getImageFile() {
    	return imageFile;
    }
    
    public void setImageFile(Part imageFile) {
    	this.imageFile = imageFile;
    }
       


    
    public String getBanheiros(){
        return banheiros;
    }
    
    public void setBanheiros(String banheiros) {
        this.banheiros = banheiros;
    }
    
    public String getQuartos(){
        return quartos;
    }
    
    public void setQuartos(String quartos) {
        this.quartos = quartos;
    }

    public String getSalas(){
        return salas;
    }
    
    public void setSalas(String salas) {
        this.salas = salas;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    
    public String getRua() {
        return rua;
    }
    
    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getCEP() {
        return CEP;
    }
    
    public void setCEP(String CEP) {
        this.CEP = CEP;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public List<Imovel> getLista() {
    	
        lista = imovelServico.recuperarImoveis();                
                
        return lista;
    }
    
    public List<Imovel> meusImoveis() {
    	
    	HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("logado");
    	
    	return getLista().stream().filter(x -> x.getUsuario().getEmail().equals(usuarioLogado.getEmail()))
    			.collect(Collectors.toList());
    }
        
    
    
    public void setImovel(Imovel imovel) {
    	
    	this.tipo = Integer.toString(imovel.getTipo());
    	this.descricao = imovel.getDescricao();
    	this.banheiros = Integer.toString(imovel.getBanheiros());
    	this.quartos = Integer.toString(imovel.getQuartos());
    	this.salas = Integer.toString(imovel.getSalas());
    	this.piscina = imovel.getPiscina();
    	this.beiraMar = imovel.getBeiraMar();
    	this.garagem = imovel.getGaragem();
    	this.salaReuniao = imovel.getSalaReuniao();
    	this.bairro = imovel.getEndereco().getBairro();
    	this.cidade = imovel.getEndereco().getCidade();
    	this.estado = imovel.getEndereco().getEstado();
    	this.numero = imovel.getEndereco().getNumero();
    	this.CEP = imovel.getEndereco().getCEP();
    	this.rua = imovel.getEndereco().getRua();
    }
    
    public Imovel getImovel() {
    	return imovel;
    }
        
    
    
    public void setLista() {
        lista = imovelServico.recuperarImoveis();
    }
    

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
          
    public String getTipo() {        
        return tipo;
    }
        
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getValor() {
        return valor;
    }
    
    public void setValor(String valor) {
        this.valor = valor;
    }    
    
    public boolean getPiscina() {
    	return piscina;
    }
    
    public void setPiscina(boolean piscina) {
    	this.piscina = piscina;
    }
    
    public boolean getGaragem() {
    	return garagem;
    }
    
    public void setGaragem(boolean garagem) {
    	this.garagem = garagem;
    }
    
    public boolean getSalaReuniao() {
    	return salaReuniao;
    }
    
    public void setSalaReuniao(boolean salaReuniao) {
    	this.salaReuniao = salaReuniao;
    }
    
    public boolean getBeiraMar() {
    	return beiraMar;
    }
    
    public void setBeiraMar(boolean beiraMar) {
    	this.beiraMar = beiraMar;
    }
    
    public UIComponent getMybutton() {
        return mybutton;
    }
    
    public void setMybutton(UIComponent mybutton) {
        this.mybutton = mybutton;
    }
    
    public int getFiltrar() {
    	return filtrar;
    }
    
    public void setFiltrar(int filtrar) {
    	this.filtrar = filtrar;
    }
    
    public String getResp() {
        return resp;
    }
    
    public void setResp(String resp) {
        this.resp = resp;
    }
    
}
