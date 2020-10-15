
var array = [];
var stop = '';
var erro = false;

function mascara(id) {
    
    var campo = document.getElementById(id);        
    campo.value = campo.value.replace( /[^\d]/g, '' );	                                         
    if ( campo.value.length > 3 ) campo.value = stop;
    else stop = campo.value;    
}

function mascaraValor(id) {
    
    var campo = document.getElementById(id);         
    campo.value = campo.value.replace( /[^\d]/g, '' )
                             .replace(/([0-9]{2})$/g, ".$1");                                               
                            
    if ( campo.value.length > 9 ) campo.value = stop;
    else stop = campo.value;        
    if(campo.value.charAt(0) == '.' ) campo.value = campo.value.replace(".", ""); 
    document.getElementById(id).value = campo.value;
}

function mascaraNumero(id) {
    
    var campo = document.getElementById(id);        
    campo.value = campo.value.replace( /[^\d]/g, '' );	                                         
    if ( campo.value.length > 7 ) campo.value = stop;
    else stop = campo.value;    
}

function mascaraCEP(id) {

    var campo = document.getElementById(id);        
    campo.value = campo.value.replace( /[^\d]/g, '' )            
                            .replace( /(\d{5})(\d)/,"$1-$2" );
    if ( campo.value.length > 9 ) campo.value = stop;
    else stop = campo.value;     
    
    if(campo.value.length < 9) erro = false;
    
    CEP(campo.value);
}


function CEP(cep) {
		
	if(cep == null || cep.length < 9) return;	
	
	preencheCampos("");
		
	cep = cep.replace("-", ""); 
		
	var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var obj = JSON.parse(this.responseText);    
                    	
    
            	if(obj.erro && !erro) {
            		alert("O CEP informado não existe!");            	
            		preencheCampos("");
            		erro = true;
            		return;
            	}
            	
            	if(obj.erro) return;
            	                 
            	preencheCampos(JSON.stringify(obj));            
        }        
    };
            xhttp.open("GET", `https://viacep.com.br/ws/${cep}/json/`);
            xhttp.send();
}


const preencheCampos = (x) => {
	
	let obj = x.length === 0 ? "" : JSON.parse(x);
	document.getElementById('form:estado').value = x.length === 0 ? "" : obj.uf;
    document.getElementById('form:cidade').value = x.length === 0 ? "" : obj.localidade;
    document.getElementById('form:bairro').value = x.length === 0 ? "" : obj.bairro;
    document.getElementById('form:rua').value = x.length === 0 ? "" : obj.logradouro;
    document.getElementById('form:numero').value = "";
}


function cidades(estado) {
			
	document.getElementById('form:cidades').value = "";                                    
	if(estado == null || estado.length === 0) return;
	
	var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var obj = JSON.parse(this.responseText);    
                 
            array = [];
            obj.map((x) => {            	
            	array.push(x.nome);
            });
                 
            document.getElementById('form:cidades').value = array.toString();                                    
        }        
    };
            xhttp.open("GET", `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estado}/municipios`);
            xhttp.send();
}

