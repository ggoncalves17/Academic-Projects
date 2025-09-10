import pygame
import os
import csv
from pygame import mixer

pygame.init()

# Cores
PRETO = (0, 0, 0)
VERDE = (0, 255, 0)

# Dimensões do ecrã
LARGURA_ECRA = 700
ALTURA_ECRA = int(LARGURA_ECRA * 0.8)

# Framerate
clock = pygame.time.Clock()
FPS = 60

screen = pygame.display.set_mode((LARGURA_ECRA, ALTURA_ECRA))
pygame.display.set_caption('Cavaleiro')

# Tempo inicial do Contador
global tempoInicial

# Variáveis do jogo
GRAVIDADE = 0.75
LINHAS = 16
COLUNAS = 150
QUANTIDADETILES = 15
NUMERONIVEIS = 2
TAMANHO_TILE = ALTURA_ECRA // LINHAS
SCROLL_THRESH = 200
scroll_tela = 0
scroll_fundo = 0
proporcaoWidth = 0.95
proporcaoHeight = 1.2
nivel = 1
inicioJogo = False
introInicial = False
volumeJogo = 0.5
mostraDefinicoes = False

# Áudios
audioSalto = pygame.mixer.Sound('audio/salto.wav')
audioSalto.set_volume(volumeJogo)

audioBackground = pygame.mixer.Sound('audio/background.mp3')
audioBackground.set_volume(volumeJogo)
audioBackground.play(-1)

# Variáveis de ação do jogador
movimentoEsquerda = False
movimentoDireita = False

# Imagens
startImagem = pygame.image.load('img/botaoIniciar.png').convert_alpha()
sairImagem = pygame.image.load('img/botaoSair.png').convert_alpha()
definicoesImagem = pygame.image.load('img/botaoDefinicoes.png').convert_alpha()
reiniciarImagem = pygame.image.load('img/botaoReiniciar.png').convert_alpha()
retornoImagem = pygame.image.load('img/botaoRetorno.png').convert_alpha()
maisVolume = pygame.image.load('img/botaoVolumeMais.png').convert_alpha()
menosVolume = pygame.image.load('img/botaoVolumeMenos.png').convert_alpha()
parabensImagem = pygame.image.load('img/parabens.png').convert_alpha()
background = pygame.image.load(
    'img/Background/background.jpeg').convert_alpha()
backgroundDefininicoes = pygame.image.load(
    'img/Background/telaDefinicoes.png')
flechaImagem = pygame.image.load('img/icons/arrow.png').convert_alpha()

# Função para colocar o background


def criarBackground():
    largura = background.get_width()
    altura = background.get_height()*1.3
    background2 = pygame.transform.scale(background, (largura, altura))
    for x in range(3):
        screen.blit(background2, ((x * largura) - scroll_fundo * 0.8, 0))


# Armazenamento dos tiles
listaTiles = []
for x in range(QUANTIDADETILES):
    imagem = pygame.image.load(f'img/Tile/{x}.png')
    imagem = pygame.transform.scale(imagem, (TAMANHO_TILE, TAMANHO_TILE))
    listaTiles.append(imagem)

# Função resetar o nível


def reiniciarNivel():
    grupoInimigo.empty()
    grupoGuerreira.empty()
    grupoSeta.empty()
    grupoMar.empty()
    grupoNivel.empty()

    lista = []
    for linha in range(LINHAS):
        l = [-1] * COLUNAS
        lista.append(l)
    return lista

# Classe Definições


class TelaDefinicoes(pygame.sprite.Sprite):
    def __init__(self, imagem, x, y):
        pygame.sprite.Sprite.__init__(self)
        self.image = imagem
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)

    def desenhar(self):
        screen.blit(self.image, self.rect.topleft)


# Classe Personagens
class Personagem(pygame.sprite.Sprite):

    def __init__(self, tipoPersonagem, x, y, scale, velocidade, setas):

        pygame.sprite.Sprite.__init__(self)

        self.tipoPersonagem = tipoPersonagem
        self.vida = 100
        self.vidaMaxima = self.vida
        self.velocidade = velocidade
        self.velocidadeY = 0
        self.direcao = 1
        self.setas = setas
        self.setas_inicial = setas
        self.seta_cooldown = 0
        self.vivo = True
        self.saltar = False
        self.ar = True
        self.flip = False
        self.parado = False

        self.indiceImagem = 0
        self.listaAnimacoes = []
        self.tempoAnimacao = pygame.time.get_ticks()
        self.acao = 0
        self.visaoInimigo = pygame.Rect(0, 0, 150, 20)

        tiposAnimacao = ['Parado', 'Correr', 'Saltar', 'Morto']

        for animacao in tiposAnimacao:

            listaAnimacoesTemporario = []
            numeroImagens = len(os.listdir(
                f'img/{self.tipoPersonagem}/{animacao}'))

            for i in range(numeroImagens):
                img = pygame.image.load(
                    f'img/{self.tipoPersonagem}/{animacao}/{i}.png').convert_alpha()
                img = pygame.transform.scale(
                    img, (int(img.get_width() * scale), int(img.get_height() * scale)))
                listaAnimacoesTemporario.append(img)
            self.listaAnimacoes.append(listaAnimacoesTemporario)

        self.image = self.listaAnimacoes[self.acao][self.indiceImagem]
        self.rect = self.image.get_rect()
        self.rect.center = (x, y)
        self.largura = self.image.get_width()
        self.altura = self.image.get_height()

    def update(self):
        self.atualizaAnimacao()
        self.verificaEstadoVida()

        if self.seta_cooldown > 0:
            self.seta_cooldown -= 1

    def verificaEstadoVida(self):
        if self.vida <= 0:
            self.vida = 0
            self.velocidade = 0
            self.vivo = False
            self.atualizaAcao(3)

    def movimentacao(self, movimentoEsquerda, movimentoDireita):
        scroll_tela = 0
        dx = 0
        dy = 0

        if movimentoEsquerda:
            dx = -self.velocidade
            self.flip = True
            self.direcao = -1

        if movimentoDireita:
            dx = self.velocidade
            self.flip = False
            self.direcao = 1

        if self.saltar == True and self.ar == False:
            self.velocidadeY = -12
            self.saltar = False
            self.ar = True

        self.velocidadeY += GRAVIDADE
        dy += self.velocidadeY

        # Verificar colisão no movimento
        for tile in mundo.listaItems:

            # Verificação de colisão na direção horizontal
            if tile[1].colliderect(self.rect.x + dx, self.rect.y, self.largura, self.altura):
                dx = 0

                # Se o inimigos baterem numa parede mudam o movimento
                if self.tipoPersonagem == 'inimigo':
                    self.direcao *= -1

            # Verificação de colisão na direção vertical
            if tile[1].colliderect(self.rect.x, self.rect.y + dy, self.largura, self.altura):

                # Verifica se é debaixo do tile
                if self.velocidadeY < 0:
                    self.velocidadeY = 0
                    dy = tile[1].bottom - self.rect.top

                # Verifica se é acima do tile
                elif self.velocidadeY >= 0:
                    self.velocidadeY = 0
                    self.ar = False
                    dy = tile[1].top - self.rect.bottom

        if pygame.sprite.spritecollide(self, grupoMar, False):
            self.vida = 0

        nivelCompleto = False
        if pygame.sprite.spritecollide(self, grupoNivel, False):
            nivelCompleto = True

        if self.rect.bottom > ALTURA_ECRA:
            self.vida = 0

        if self.tipoPersonagem == 'cavaleiro':
            if self.rect.left + dx < 0 or self.rect.right + dx > LARGURA_ECRA:
                dx = 0

        self.rect.x += dx
        self.rect.y += dy

        if self.tipoPersonagem == 'cavaleiro':
            if (self.rect.right > LARGURA_ECRA - SCROLL_THRESH and scroll_fundo < (mundo.tamanhoMundo * TAMANHO_TILE) - LARGURA_ECRA)\
                    or (self.rect.left < SCROLL_THRESH and scroll_fundo > abs(dx)):
                self.rect.x -= dx
                scroll_tela = -dx

        return scroll_tela, nivelCompleto

    def inimigo(self):
        if self.vivo and cavaleiro.vivo:
            if self.visaoInimigo.colliderect(cavaleiro.rect):
                self.atualizaAcao(0)
                if cavaleiro.rect.x < self.rect.x:
                    self.direcao = -1
                    self.flip = True
                else:
                    self.direcao = 1
                    self.flip = False
                self.atirar()
            else:
                if self.direcao == 1:
                    movimentoDireitaInimigo = True
                else:
                    movimentoDireitaInimigo = False
                movimentoEsquerdaInimigo = not movimentoDireitaInimigo
                self.movimentacao(movimentoEsquerdaInimigo,
                                  movimentoDireitaInimigo)
                self.atualizaAcao(1)

                # 100 é o raio de alcance do inimigo
                self.visaoInimigo.center = (
                    self.rect.centerx + 100 * self.direcao, self.rect.centery)

        self.rect.x += scroll_tela

    def atirar(self):
        if self.seta_cooldown == 0 and self.setas > 0:
            self.seta_cooldown = 15
            seta = Seta(self.rect.centerx + (0.75 *
                        self.rect.size[0] * self.direcao), self.rect.centery, self.direcao)
            grupoSeta.add(seta)
            self.setas -= 1

    def atualizaAnimacao(self):

        ANIMATION_COOLDOWN = 80

        self.image = self.listaAnimacoes[self.acao][self.indiceImagem]

        if pygame.time.get_ticks() - self.tempoAnimacao > ANIMATION_COOLDOWN:
            self.tempoAnimacao = pygame.time.get_ticks()
            self.indiceImagem += 1

        if self.indiceImagem >= len(self.listaAnimacoes[self.acao]):
            if self.acao == 3:
                self.indiceImagem = len(self.listaAnimacoes[self.acao]) - 1
            else:
                self.indiceImagem = 0

    def atualizaAcao(self, novaAcao):
        if novaAcao != self.acao:
            self.acao = novaAcao

            self.indiceImagem = 0
            self.tempoAnimacao = pygame.time.get_ticks()

    def desenhar(self):
        screen.blit(pygame.transform.flip(
            self.image, self.flip, False), self.rect)


class Mundo():
    def __init__(self):
        self.listaItems = []

    def criaMundo(self, dados):

        self.tamanhoMundo = len(dados[0])

        for y, linha in enumerate(dados):
            for x, tile in enumerate(linha):
                if tile >= 0:
                    imagem = listaTiles[tile]
                    imagem_rect = imagem.get_rect()
                    imagem_rect.x = x * TAMANHO_TILE
                    imagem_rect.y = y * TAMANHO_TILE
                    dadosTile = (imagem, imagem_rect)

                    if tile >= 0 and tile <= 8:
                        self.listaItems.append(dadosTile)

                    elif tile >= 9 and tile <= 10:
                        mar = Mar(imagem, x * TAMANHO_TILE, y * TAMANHO_TILE)
                        grupoMar.add(mar)

                    elif tile == 11:
                        cavaleiro = Personagem(
                            'cavaleiro', x * TAMANHO_TILE, y * TAMANHO_TILE, 1.65, 5, 20)
                        barraVida = BarraVida(
                            10, 10, cavaleiro.vida, cavaleiro.vida)

                    elif tile == 12:
                        inimigo = Personagem(
                            'inimigo', x * TAMANHO_TILE, y * TAMANHO_TILE, 1.65, 2, 20)
                        grupoInimigo.add(inimigo)

                    elif tile == 13:
                        princesa = Sair(
                            imagem, x * TAMANHO_TILE, y * TAMANHO_TILE)
                        grupoGuerreira.add(princesa)

                    elif tile == 14:
                        exit = Sair(imagem, x * TAMANHO_TILE, y * TAMANHO_TILE)
                        grupoNivel.add(exit)
        return cavaleiro, barraVida

    def desenhar(self):
        for tile in self.listaItems:
            tile[1][0] += scroll_tela
            screen.blit(tile[0], tile[1])


class Mar(pygame.sprite.Sprite):
    def __init__(self, imagem, x, y):
        pygame.sprite.Sprite.__init__(self)
        self.image = imagem
        self.rect = self.image.get_rect()
        self.rect.midtop = (x + TAMANHO_TILE // 2, y +
                            (TAMANHO_TILE - self.image.get_height()))

    def update(self):
        self.rect.x += scroll_tela


class Sair(pygame.sprite.Sprite):
    def __init__(self, imagem, x, y):
        pygame.sprite.Sprite.__init__(self)
        self.image = imagem
        self.rect = self.image.get_rect()
        self.rect.midtop = (x + TAMANHO_TILE // 2, y +
                            (TAMANHO_TILE - self.image.get_height()))

    def update(self):
        self.rect.x += scroll_tela


class BarraVida():
    def __init__(self, x, y, vida, vidaMaxima):
        self.x = x
        self.y = y
        self.vida = vida
        self.vidaMaxima = vidaMaxima

    def desenharVida(self, vida):

        self.vida = vida

        proporção = self.vida / self.vidaMaxima
        pygame.draw.rect(screen, PRETO, (self.x - 2, self.y - 2, 154, 24))
        pygame.draw.rect(screen, VERDE, (self.x, self.y, 150 * proporção, 20))


class Seta(pygame.sprite.Sprite):
    def __init__(self, x, y, direcao):
        pygame.sprite.Sprite.__init__(self)
        self.velocidade = 7
        self.image = flechaImagem
        self.rect = self.image.get_rect()
        self.rect.center = (x, y)
        self.direcao = direcao

    def update(self):

        self.rect.x += (self.direcao * self.velocidade) + scroll_tela

        if self.rect.right < 0 or self.rect.left > LARGURA_ECRA:
            self.kill()
        for tile in mundo.listaItems:
            if tile[1].colliderect(self.rect):
                self.kill()
        if pygame.sprite.spritecollide(cavaleiro, grupoSeta, False):
            if cavaleiro.vivo:
                cavaleiro.vida -= 4
                self.kill()


class Botao():
    def __init__(self, x, y, imagem, scale):
        largura = imagem.get_width()
        altura = imagem.get_height()
        self.image = pygame.transform.scale(
            imagem, (int(largura * scale), int(altura * scale)))
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)
        self.pressionado = False

    def desenharBotao(self, tela):
        acao = False
        posicao = pygame.mouse.get_pos()

        if self.rect.collidepoint(posicao):
            if pygame.mouse.get_pressed()[0] == 1 and self.pressionado == False:
                acao = True
                self.pressionado = True

        if pygame.mouse.get_pressed()[0] == 0:
            self.pressionado = False

        tela.blit(self.image, (self.rect.x, self.rect.y))
        return acao


# Criação dos botões
botaoIniciar = Botao(LARGURA_ECRA // 4 - (startImagem.get_width() //
                     2), ALTURA_ECRA // 2 - 100, startImagem, 1)
botaoDefinicoes = Botao(LARGURA_ECRA // 4 - (definicoesImagem.get_width() //
                        2), ALTURA_ECRA // 2, definicoesImagem, 1)
botaoSair = Botao(LARGURA_ECRA // 4 - (sairImagem.get_width() //
                  2), ALTURA_ECRA // 2 + 100, sairImagem, 1)
botaoReiniciar = Botao(LARGURA_ECRA // 2 - (reiniciarImagem.get_width() //
                       2), ALTURA_ECRA // 2 - 100, reiniciarImagem, 1)
botaoVitoria = Botao(LARGURA_ECRA // 2 - (parabensImagem.get_width() //
                     2), ALTURA_ECRA // 2 - 50, parabensImagem, 1)

# Criação dos grupos das sprites
grupoInimigo = pygame.sprite.Group()
grupoSeta = pygame.sprite.Group()
grupoMar = pygame.sprite.Group()
grupoNivel = pygame.sprite.Group()
grupoGuerreira = pygame.sprite.Group()


def configuraEcra(imagemFundo):
    screen.fill(PRETO)
    fundo = pygame.image.load(imagemFundo)
    largura = fundo.get_width() * proporcaoWidth
    altura = fundo.get_height() * proporcaoHeight
    fundoFinal = pygame.transform.scale(
        fundo, (largura, altura))
    screen.blit(fundoFinal, (0, 0))


def carregarDadosNivel(nivel):
    dadosNivel = reiniciarNivel()
    with open(f'dadosNivel{nivel}.csv', newline='') as csvfile:
        leitura = csv.reader(csvfile, delimiter=',')
        for x, row in enumerate(leitura):
            for y, tile in enumerate(row):
                dadosNivel[x][y] = int(tile)
    return dadosNivel


def contadorTempo(tempo_decorrido):
    fonte = pygame.font.Font(None, 36)
    texto = fonte.render(f'{tempo_decorrido} segundos', True, PRETO)
    screen.blit(texto, (LARGURA_ECRA - 150, 20))


dadosMundo = []
for row in range(LINHAS):
    r = [-1] * COLUNAS
    dadosMundo.append(r)

with open(f'dadosNivel{nivel}.csv', newline='') as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    for x, row in enumerate(reader):
        for y, tile in enumerate(row):
            dadosMundo[x][y] = int(tile)
mundo = Mundo()
cavaleiro, barraVida = mundo.criaMundo(dadosMundo)

tempo_final = False
validaSomVitoria = False
tempo_decorrido = 0
run = True
verificacao = True

while run:

    clock.tick(FPS)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False

        if event.type == pygame.KEYDOWN and inicioJogo:
            if event.key == pygame.K_LEFT:
                movimentoEsquerda = True
            if event.key == pygame.K_RIGHT:
                movimentoDireita = True
            if event.key == pygame.K_UP and cavaleiro.vivo:
                cavaleiro.saltar = True
                audioSalto.play()
            if event.key == pygame.K_ESCAPE:
                run = False

        if event.type == pygame.KEYUP and inicioJogo:
            if event.key == pygame.K_LEFT:
                movimentoEsquerda = False
            if event.key == pygame.K_RIGHT:
                movimentoDireita = False
            if event.key == pygame.K_UP and cavaleiro.vivo:
                cavaleiro.saltar = False

    if inicioJogo == False:
        if verificacao == True:
            configuraEcra('img/background/telaInicial.png')
            if botaoIniciar.desenharBotao(screen):
                inicioJogo = True
                introInicial = True
                tempoInicial = pygame.time.get_ticks()

            if botaoDefinicoes.desenharBotao(screen):
                mostraDefinicoes = True
                verificacao = False

            if botaoSair.desenharBotao(screen):
                run = False

    else:
        tempo_atual = pygame.time.get_ticks()
        tempo_decorrido = (tempo_atual - tempoInicial) // 1000

        criarBackground()

        mundo.desenhar()

        barraVida.desenharVida(cavaleiro.vida)

        cavaleiro.update()
        cavaleiro.desenhar()

        for inimigo in grupoInimigo:
            inimigo.inimigo()
            inimigo.update()
            inimigo.desenhar()

        grupoSeta.update()
        grupoMar.update()
        grupoNivel.update()
        grupoGuerreira.update()
        grupoSeta.draw(screen)
        grupoMar.draw(screen)
        grupoNivel.draw(screen)
        grupoGuerreira.draw(screen)

        if cavaleiro.vivo:
            if cavaleiro.ar:
                cavaleiro.atualizaAcao(2)
            elif movimentoEsquerda or movimentoDireita:
                cavaleiro.atualizaAcao(1)
            else:
                cavaleiro.atualizaAcao(0)
            scroll_tela, nivelCompleto = cavaleiro.movimentacao(
                movimentoEsquerda, movimentoDireita)
            scroll_fundo -= scroll_tela

            if nivelCompleto:
                introInicial = True
                scroll_fundo = 0
                nivel += 1
                if nivel <= NUMERONIVEIS:
                    dadosMundo = carregarDadosNivel(nivel)
                    mundo = Mundo()
                    cavaleiro, barraVida = mundo.criaMundo(dadosMundo)
                else:
                    if tempo_final == False:
                        tempo_total = tempo_decorrido
                        tempo_final = True

                    audioBackground.stop()

                    if validaSomVitoria == False:
                        audioVitoria = pygame.mixer.Sound('audio/vitoria.mp3')
                        audioVitoria.set_volume(0.7)
                        audioVitoria.play(-1)
                        validaSomVitoria = True

                    screen.fill(PRETO)

                    fonte = pygame.font.Font(None, 36)
                    texto = fonte.render(
                        f'Tempo total demorado: {tempo_total} segundos', True, VERDE)
                    screen.blit(
                        texto, (LARGURA_ECRA // 2 - 200, ALTURA_ECRA // 2))

                    botaoVitoria = Botao(
                        LARGURA_ECRA // 2 - (parabensImagem.get_width() // 2), ALTURA_ECRA // 2 - 200, parabensImagem, 1)
                    botaoSair = Botao(
                        LARGURA_ECRA // 2 - (sairImagem.get_width() // 2), ALTURA_ECRA // 2 + 100, sairImagem, 1)

                    botaoVitoria.desenharBotao(screen)

                    if botaoSair.desenharBotao(screen):
                        run = False

        else:
            scroll_tela = 0
            if botaoReiniciar.desenharBotao(screen):
                introInicial = True
                scroll_fundo = 0
                dadosMundo = carregarDadosNivel(nivel)
                mundo = Mundo()
                cavaleiro, barraVida = mundo.criaMundo(dadosMundo)

        contadorTempo(tempo_decorrido)

    if mostraDefinicoes:
        configuraEcra('img/background/telaDefinicoes.png')

        botaoRetorno = Botao(LARGURA_ECRA // 2 - (retornoImagem.get_width() //
                             2), ALTURA_ECRA // 2 + 100, retornoImagem, 1)
        botaoVolumeMais = Botao(
            LARGURA_ECRA // 2 + (maisVolume.get_width() // 2), ALTURA_ECRA // 2 - maisVolume.get_height(), maisVolume, 1)
        botaoVolumeMenos = Botao(
            LARGURA_ECRA // 4 + (menosVolume.get_width() // 2), ALTURA_ECRA // 2 - menosVolume.get_height(), menosVolume, 1)

        if botaoVolumeMais.desenharBotao(screen):
            volumeJogo += 0.1
            audioBackground.set_volume(volumeJogo)
            audioSalto.set_volume(volumeJogo)
        if botaoVolumeMenos.desenharBotao(screen):
            volumeJogo -= 0.1
            audioBackground.set_volume(volumeJogo)
            audioSalto.set_volume(volumeJogo)
        if botaoRetorno.desenharBotao(screen):
            mostraDefinicoes = False
            verificacao = True

    pygame.display.update()

pygame.quit()
