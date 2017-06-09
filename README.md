# Agnostic Vault

Poc de uma vault simples com encriptação e fornecimento de secrets e armazenamento de arquivos/blobs encriptados ou plenos. Foram criadas duas implementações para a Vault:
*Azure KeyVault*= (Não testada). Utiliza um secret guardado na Azure Key Vault para gerar uma encriptação AES.
*Vault Project*= (Testada com arquivos textos). Utiliza o transit do vault para gerar arquivos encriptados como serviço.

Para o Storage, foi criado uma implementação para o Storage da Azure.
O storage armazena um objeto customizado com metadados relativos ao encriptamento.

# Utilização do KeyVault
Com Azure KeyVault:

1. Criar uma Key Vault no Azure
2. Adicionar um Secret no Key Vault
3. Registrar uma aplicação no Azure Active Directory
4. Autorizar esta aplicaço a utilizar o secredo criado no Key Vault
5. Configurar o application properties com o id e segredo do aplicação (zup.keyvault.auth.azure.clientid, zup.keyvault.auth.azure.secret)

OBS: essa implementação ainda não foi testada e necessitaria descomentar o @Component para utilizá-la (//TODO).

Com o Vault Projet
1. Instalar o vault na máquina
2. Iniciar em modo dev
3. Mountar o backend transit
4. Criar uma chave no transit (ex: credicard)
5. Configurar o application properties (zup.keyvault.baseurl, zup.keyvault.port, zup.keyvault.auth.token)

OBS: em modo dev, as informações não são persistidas. É necessário ajustes para funcionar em produção.
OBS2: nos testes não foi possível recuperar um arquivo de imagem (TODO: estudar possível problema com byte[] em chamadas rest). No entanto arquivos de texto funcionam corretamente.

# Storage
É possível utilizar a storage da azure ou implementar qualquer outra (extendendo AbstractStorageService)
Com o azure:
1. Criar um storage no azure.
2. Criar um container
3. Configurar o aplication properties (zup.keyvault.storage.container, zup.keyvault.storage.account, zup.keyvault.storage.key)

# API da POC
A api é utilizada em http sem sequer autenticação e se destina somente para POC localhost, não utilizar a mesma em PROD.

*GET http://localhost:8080/keys*: Retorna em json a lista de chaves disponíveis no vault (referência das chaves, não o valor).
OBS: azure vault não implementado (necessário pensar como retornar referencias de secrets)

*POST http://localhost:8080//store/encryptation/{keyid}*. Encripta e armazena um arquivo no storage configurado. O id do arquivo será seu nome (fileid)
RequestParam: file (multipart/form-data)
Resposta:204

*GET http://localhost:8080//store/{fileid}/decryption/{keyid}* Retorna o arquivo encriptado anteriormente

TODO: get secret, delete file
