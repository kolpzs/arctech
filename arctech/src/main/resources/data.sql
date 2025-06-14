-- Limpeza em ordem de dependência
DELETE FROM contas;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM categorias;
DELETE FROM instituicoes;


-- Inserir os papéis (Roles)
INSERT INTO roles(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO roles(id, name) VALUES(2, 'ROLE_ADMIN');

-- Inserir Usuários (senhas são 'user123' e 'admin123' - serão criptografadas pela aplicação ao registrar)
-- NOTA: O ideal é registrar os usuários via API para ter a senha criptografada corretamente.
-- Estes inserts são apenas para referência da estrutura.
-- Vamos criar os usuários via API para garantir a criptografia.

-- Inserir Instituições
INSERT INTO instituicoes (id, nome) VALUES
                                        (1, 'Nubank'),
                                        (2, 'Bradesco'),
                                        (3, 'Dinheiro Físico');

-- Inserir Categorias
INSERT INTO categorias (id, nome) VALUES
                                      (1, 'Salário'),
                                      (2, 'Alimentação'),
                                      (3, 'Transporte'),
                                      (4, 'Lazer'),
                                      (5, 'Moradia');

-- Contas serão adicionadas depois, associadas a usuários específicos.