# Agnostic Vault

Poc de uma vault simples com encriptação e fornecimento de secrets e armazenamento de arquivos/blobs encriptados (ou não). Foram criadas duas implementações para a Vault:
*Azure KeyVault*= (Não testada). Utiliza um secret guardado na Azure Key Vault para gerar uma encriptação AES.
*Vault Project*= (Testada com arquivos textos). Utiliza o transit do vault para gerar arquivos encriptados como serviço.

Para o Storage, foi criado uma implementação para o Storage da Azure.
O storage armazena um objeto customizado com metadados relativos ao encriptamento.
