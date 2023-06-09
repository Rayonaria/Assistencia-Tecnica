PGDMP                         {            Tecnica    14.2    14.2 #               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    25104    Tecnica    DATABASE     i   CREATE DATABASE "Tecnica" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "Tecnica";
                postgres    false                        2615    25105    sistema    SCHEMA        CREATE SCHEMA sistema;
    DROP SCHEMA sistema;
                postgres    false            �            1259    25154    cliente    TABLE     (  CREATE TABLE sistema.cliente (
    idcli integer NOT NULL,
    nomecli character varying(50) NOT NULL,
    endcli character varying(100),
    fonecli character varying(15) NOT NULL,
    emailcli character varying(50),
    bairrocli character varying(100),
    cidadecli character varying(100)
);
    DROP TABLE sistema.cliente;
       sistema         heap    postgres    false    4            �            1259    25153    cliente_idcli_seq    SEQUENCE     �   CREATE SEQUENCE sistema.cliente_idcli_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE sistema.cliente_idcli_seq;
       sistema          postgres    false    4    211                       0    0    cliente_idcli_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE sistema.cliente_idcli_seq OWNED BY sistema.cliente.idcli;
          sistema          postgres    false    210            �            1259    25216    os    TABLE     �  CREATE TABLE sistema.os (
    os integer NOT NULL,
    data_os timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    tipo character varying(150) NOT NULL,
    situacao character varying(20) NOT NULL,
    equipamento character varying(150) NOT NULL,
    defeito character varying(150),
    servico character varying(150),
    tecnico character varying(30),
    valor character varying(20),
    idcli integer NOT NULL
);
    DROP TABLE sistema.os;
       sistema         heap    postgres    false    4            �            1259    25215 	   os_os_seq    SEQUENCE     �   CREATE SEQUENCE sistema.os_os_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE sistema.os_os_seq;
       sistema          postgres    false    213    4                       0    0 	   os_os_seq    SEQUENCE OWNED BY     9   ALTER SEQUENCE sistema.os_os_seq OWNED BY sistema.os.os;
          sistema          postgres    false    212            �            1259    25238    produto    TABLE     �   CREATE TABLE sistema.produto (
    cod integer NOT NULL,
    nome character varying(100) NOT NULL,
    precocompra character varying(50) NOT NULL,
    quant character varying(15) NOT NULL,
    provenda character varying(50) NOT NULL
);
    DROP TABLE sistema.produto;
       sistema         heap    postgres    false    4            �            1259    25237    produto_cod_seq    SEQUENCE     �   CREATE SEQUENCE sistema.produto_cod_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE sistema.produto_cod_seq;
       sistema          postgres    false    4    217                       0    0    produto_cod_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE sistema.produto_cod_seq OWNED BY sistema.produto.cod;
          sistema          postgres    false    216            �            1259    25231    usuario    TABLE       CREATE TABLE sistema.usuario (
    id integer NOT NULL,
    nome character varying(15) NOT NULL,
    fone character varying(15),
    login character varying(50) NOT NULL,
    senha character varying(250) NOT NULL,
    perfil character varying(20) NOT NULL
);
    DROP TABLE sistema.usuario;
       sistema         heap    postgres    false    4            �            1259    25230    usuario_id_seq    SEQUENCE     �   CREATE SEQUENCE sistema.usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE sistema.usuario_id_seq;
       sistema          postgres    false    4    215                       0    0    usuario_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE sistema.usuario_id_seq OWNED BY sistema.usuario.id;
          sistema          postgres    false    214            l           2604    25157    cliente idcli    DEFAULT     p   ALTER TABLE ONLY sistema.cliente ALTER COLUMN idcli SET DEFAULT nextval('sistema.cliente_idcli_seq'::regclass);
 =   ALTER TABLE sistema.cliente ALTER COLUMN idcli DROP DEFAULT;
       sistema          postgres    false    210    211    211            m           2604    25219    os os    DEFAULT     `   ALTER TABLE ONLY sistema.os ALTER COLUMN os SET DEFAULT nextval('sistema.os_os_seq'::regclass);
 5   ALTER TABLE sistema.os ALTER COLUMN os DROP DEFAULT;
       sistema          postgres    false    213    212    213            p           2604    25241    produto cod    DEFAULT     l   ALTER TABLE ONLY sistema.produto ALTER COLUMN cod SET DEFAULT nextval('sistema.produto_cod_seq'::regclass);
 ;   ALTER TABLE sistema.produto ALTER COLUMN cod DROP DEFAULT;
       sistema          postgres    false    216    217    217            o           2604    25234 
   usuario id    DEFAULT     j   ALTER TABLE ONLY sistema.usuario ALTER COLUMN id SET DEFAULT nextval('sistema.usuario_id_seq'::regclass);
 :   ALTER TABLE sistema.usuario ALTER COLUMN id DROP DEFAULT;
       sistema          postgres    false    214    215    215                      0    25154    cliente 
   TABLE DATA           c   COPY sistema.cliente (idcli, nomecli, endcli, fonecli, emailcli, bairrocli, cidadecli) FROM stdin;
    sistema          postgres    false    211   �&       
          0    25216    os 
   TABLE DATA           p   COPY sistema.os (os, data_os, tipo, situacao, equipamento, defeito, servico, tecnico, valor, idcli) FROM stdin;
    sistema          postgres    false    213   �'                 0    25238    produto 
   TABLE DATA           K   COPY sistema.produto (cod, nome, precocompra, quant, provenda) FROM stdin;
    sistema          postgres    false    217   k)                 0    25231    usuario 
   TABLE DATA           H   COPY sistema.usuario (id, nome, fone, login, senha, perfil) FROM stdin;
    sistema          postgres    false    215   �)                  0    0    cliente_idcli_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('sistema.cliente_idcli_seq', 13, true);
          sistema          postgres    false    210                       0    0 	   os_os_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('sistema.os_os_seq', 15, true);
          sistema          postgres    false    212                       0    0    produto_cod_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('sistema.produto_cod_seq', 5, true);
          sistema          postgres    false    216                       0    0    usuario_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('sistema.usuario_id_seq', 10, true);
          sistema          postgres    false    214            r           2606    25161    cliente cliente_emailcli_key 
   CONSTRAINT     \   ALTER TABLE ONLY sistema.cliente
    ADD CONSTRAINT cliente_emailcli_key UNIQUE (emailcli);
 G   ALTER TABLE ONLY sistema.cliente DROP CONSTRAINT cliente_emailcli_key;
       sistema            postgres    false    211            t           2606    25159    cliente cliente_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY sistema.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (idcli);
 ?   ALTER TABLE ONLY sistema.cliente DROP CONSTRAINT cliente_pkey;
       sistema            postgres    false    211            v           2606    25224 
   os os_pkey 
   CONSTRAINT     I   ALTER TABLE ONLY sistema.os
    ADD CONSTRAINT os_pkey PRIMARY KEY (os);
 5   ALTER TABLE ONLY sistema.os DROP CONSTRAINT os_pkey;
       sistema            postgres    false    213            z           2606    25243    produto produto_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY sistema.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (cod);
 ?   ALTER TABLE ONLY sistema.produto DROP CONSTRAINT produto_pkey;
       sistema            postgres    false    217            x           2606    25236    usuario usuario_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY sistema.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);
 ?   ALTER TABLE ONLY sistema.usuario DROP CONSTRAINT usuario_pkey;
       sistema            postgres    false    215            {           2606    25225    os os_idcli_fkey    FK CONSTRAINT     t   ALTER TABLE ONLY sistema.os
    ADD CONSTRAINT os_idcli_fkey FOREIGN KEY (idcli) REFERENCES sistema.cliente(idcli);
 ;   ALTER TABLE ONLY sistema.os DROP CONSTRAINT os_idcli_fkey;
       sistema          postgres    false    3188    213    211                 x�e�An� D��)�@����K�ʋJ��v�&�E���s��z�\����,�fx�h�3���x����^�^�X��p��`3E}�:��09��s�V1K����f�5Ciȑ��G�f�DUū���lI���3���k��,B��*r4h�KIu>�pb%��鼔P���"������~�"ޘn֖Ux�"�O�6�Ir��ʖ���N:K#�I�xH�L�ښ���T��(��k����)��~�3�O�u�x�n���c?8c��      
   �  x���Mn�0F��S� �0�Q�.H�n�6h�솶W1%��d�'ʪ���J�v$@k�� �7�
y��J.�j����2XV�5�]��1��~L~��M��a:�n|��_Ÿ��؏���|۹|>��v���xw�IH�D�\��Y�V�Bai$e`���O����z3�Ը�����jv�b}h?s��9Au^+K�
*��Z��]����0����� �+V�$��$-�V�� ֤�㭋��é���u���ٳ$��7�y0B���ߊ���M���,i��)��iڍn]���N�b$zɅ���TF����8��m�m�����(g2�K��X[�V��ֈU}�{��I�г<·�����d�^#�-�R��I�y?����ձ�{�M>xX�w6����1��� �         m   x�M���0���=�=��^)�F7�F��,i��=�߇b���_��})[b'N.��Z>��*FA����-=W���֎wf/2Û�����\k�0���� 4         �   x�]NA�0<OC,���8y�x� bmI����u�0����xz�x��'�Pk�cc����sQ�B�[2C�x�J�d.1��y!w��;B9Z3��,��(�
�.8r-��ꇟ�L�44C���O�Rh����>��[�{'-L�     