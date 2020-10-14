
var array = [];

var stop = '';
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
    if ( campo.value.length > 5 ) campo.value = stop;
    else stop = campo.value;    
}

function mascaraCEP(id) {

    var campo = document.getElementById(id);        
    campo.value = campo.value.replace( /[^\d]/g, '' )            
                            .replace( /(\d{5})(\d)/,"$1-$2" );
    if ( campo.value.length > 9 ) campo.value = stop;
    else stop = campo.value;    
}



function CEP(cep) {
		
	if(cep == null || cep.length < 9) return;
	
	cep = cep.replace("-", ""); 
	
	var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var obj = JSON.parse(this.responseText);    
                 
            	if(obj.erro) {
            		alert("O CEP informado não existe!");
            		return;
            	}
                 
            document.getElementById('form:estado').value = obj.uf;
            document.getElementById('form:cidade').value = obj.localidade;
            document.getElementById('form:bairro').value = obj.bairro;
            document.getElementById('form:rua').value = obj.logradouro;
        }        
    };
            xhttp.open("GET", `https://viacep.com.br/ws/${cep}/json/`);
            xhttp.send();
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

