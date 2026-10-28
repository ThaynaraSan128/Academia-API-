# API de Academia

API REST em Java 17, Spring Boot, Spring Data JPA e H2. Por padrão, os dados ficam no arquivo `./data/academia.mv.db`, preservados entre execuções. O Hibernate cria/atualiza as tabelas automaticamente.

## Executar

Na raiz do projeto:

```powershell
./mvnw.cmd spring-boot:run
```

Base URL: `http://localhost:8080/api`. A console H2 fica em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:file:./data/academia`, usuário `sa`, senha em branco).

## Testes no Postman

Configure `Content-Type: application/json` para os POSTs. Cadastre nesta ordem: professor, aluno, exercícios e então treino. Use os IDs devolvidos por cada criação nos próximos payloads.

### Criar professor — `POST /professores`

```json
{"nome":"Marina Costa","email":"marina@academia.com","especialidade":"Musculação"}
```

### Criar aluno — `POST /alunos`

```json
{"nome":"João Silva","email":"joao@email.com","cpf":"12345678900","dataNascimento":"1998-05-10","inicioMatricula":"2026-01-01","fimMatricula":"2027-01-01"}
```

As datas são `AAAA-MM-DD`. Para criar um treino, a matrícula precisa cobrir a data do treino (hoje por padrão).

### Criar exercícios — `POST /exercicios`

```json
{"nome":"Supino reto","grupoMuscular":"Peito","descricao":"Barra no banco reto"}
```

```json
{"nome":"Agachamento livre","grupoMuscular":"Pernas","descricao":"Agachamento com barra"}
```

### Criar ficha — `POST /treinos`

Troque os IDs de exemplo pelos IDs recebidos acima. `dataCriacao` é opcional; se omitida, usa a data atual.

```json
{"nome":"Treino A - Peito e pernas","observacoes":"Aumentar carga gradualmente","alunoId":1,"professorId":1,"exercicios":[{"exercicioId":1,"series":4,"repeticoes":10,"cargaKg":30,"descansoSegundos":90},{"exercicioId":2,"series":3,"repeticoes":12,"cargaKg":40,"descansoSegundos":90}]}
```

## Endpoints

| Método | Caminho | Uso |
| --- | --- | --- |
| POST | `/api/alunos` | Cadastrar aluno |
| GET | `/api/alunos` | Listar alunos |
| GET | `/api/alunos/{id}` | Consultar aluno |
| GET | `/api/alunos/matriculas/ativas` | Listar matrículas ativas hoje |
| GET | `/api/alunos/matriculas/vencidas` | Listar matrículas vencidas |
| GET | `/api/alunos/{id}/matricula` | Consultar situação e dias restantes |
| POST | `/api/professores` | Cadastrar professor |
| GET | `/api/professores` | Listar professores |
| GET | `/api/professores/{id}` | Consultar professor |
| POST | `/api/exercicios` | Cadastrar exercício |
| GET | `/api/exercicios` | Listar exercícios |
| GET | `/api/exercicios/{id}` | Consultar exercício |
| POST | `/api/treinos` | Criar ficha associada a aluno, professor e exercícios |
| GET | `/api/treinos/{id}` | Consultar ficha |
| GET | `/api/alunos/{id}/treinos` | Histórico do aluno, mais recente primeiro |
| GET | `/api/alunos/{id}/treinos?inicio=2026-01-01&fim=2026-12-31` | Filtrar histórico por período (informe ambos) |

Respostas de criação usam HTTP 201, consultas usam HTTP 200, payload inválido retorna 400, cadastro duplicado 409 e ID inexistente 404. E-mail e CPF de aluno são únicos; e-mail de professor e nome do exercício também. As matrículas incluem a data de início e a data final. Não é permitido criar treino para matrícula inativa ou data futura.
