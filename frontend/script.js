document.getElementById('formPessoa').addEventListener('submit', async(event)=>{
    event.preventDefault();

    const nome = document.getElementById('nome').value;
    const cpf = document.getElementById('cpf').value;
    const telefone = document.getElementById('telefone').value;

    const data = {
        nome: nome,
        cpf: cpf,
        telefone: telefone
    };

    try{
        const response = await fetch('http://localhost:8081/api/post',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if(response.ok){
            document.getElementById('responseMessage').innerText = 'Formulário enviado com sucesso!';
            document.getElementById('formPessoa').reset();
        }else{
            document.getElementById('responseMessage').innerText = `Erro: ${result.message}`;
        }
    }catch(error){
        document.getElementById('responseMessage').innerText = '';
        document.getElementById('formPessoa').innerText = 'Erro na comunicação com o servidor.';
    }
});

// Função para formatar o CPF
function formatCPF(value) {
    value = value.replace(/\D/g, '');
    if (value.length <= 11) {
        value = value.replace(/(\d{3})(\d{3})?(\d{3})?(\d{1,2})?/, function (_, p1, p2, p3, p4) {
            if (p2) {
                return p1 + '.' + p2 + (p3 ? '.' + p3 : '') + (p4 ? '-' + p4 : '');
            }
            return p1 + (p2 ? '.' + p2 : '') + (p3 ? '.' + p3 : '') + (p4 ? '-' + p4 : '');
        });
    }
    return value;
}

// Função para formatar o telefone
function formatTelefone(value) {
    value = value.replace(/\D/g, '');
    if (value.length <= 16) {
        value = value.replace(/(\d{2})(\d)?(\d{4})(\d{0,4})/, function (_, p1, p2, p3, p4) {
            return '(' + p1 + ') ' + (p2 ? p2 + ' ' : '') + p3 + (p4 ? '-' + p4 : '');
        });
    }
    return value;
}

// Adiciona o evento de input para formatar o CPF
document.getElementById('cpf').addEventListener('input', function () {
    this.value = formatCPF(this.value);
});

// Adiciona o evento de input para formatar o telefone
document.getElementById('telefone').addEventListener('input', function () {
    this.value = formatTelefone(this.value);
});

