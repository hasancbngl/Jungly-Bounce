package com.cobanogluhasan.junglybounce;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;




public class MyGdxGame extends ApplicationAdapter {
	public MyGdxGame() {

	}

	public interface IActivityRequestHandler {

		void showOrLoadInterstitial();
	}



	public IActivityRequestHandler requestHandler;

	public MyGdxGame(IActivityRequestHandler requestHandler){
		this.requestHandler=requestHandler;
	}



	public static final int GAME_WIDTH=1080;
	public static final int GAME_HEIGHT=1920;


	SpriteBatch batch;
	Texture background;
	Texture ball;
	Texture gameOver;
	Music music;

	float ballY;
	float velocity=0;
	int gameState=0;
	Random random;
	float cactusVelocity=5;
	int numberOfCactus=4;
	float[] cactusX = new float[numberOfCactus];
	float[] cactusTopX= new float[numberOfCactus];
	float spaceBetweenCactus=250;
	Circle ballCircle;
	int score=0;
	int scoringCactus=0;


	//  ShapeRenderer shapeRenderer;

	Rectangle[] bottomCactusRectangles;
	Rectangle[] bottomCactusRectangles1;

	Rectangle[] topCactusRectangles;
	Rectangle[] topCactusRectangles1;

	BitmapFont bitmapFont;
	BitmapFont bitmapFont1;
	BitmapFont bitmapFont2;

	float distanceBetweenCactus=0;
	FileHandle dirBottom;
	FileHandle dirTop;

	Texture[] randomTextureBottom = new Texture[numberOfCactus];
	Texture[] randomTextureTop = new Texture[numberOfCactus];
	Texture[] randomTextureBottom1 = new Texture[numberOfCactus];
	Texture[] randomTextureTop1 = new Texture[numberOfCactus];

	Texture  bottomCactus;

	private OrthographicCamera camera;
    private FitViewport viewport;

	@Override
	public void create () {


		camera = new OrthographicCamera();
		viewport = new FitViewport(GAME_WIDTH,GAME_HEIGHT,camera) ;
		camera.position.set( viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

		camera.update();


		batch = new SpriteBatch();
		background = new Texture("full_background.png");

		gameOver=new Texture("gameover.png");

		ball = new Texture("Tennisball.png");


		bottomCactus=new Texture("cactus1bottom.png");
		ballCircle=new Circle();
		//  shapeRenderer=new ShapeRenderer();

		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.BLUE);
		bitmapFont.getData().setScale(8);

		bitmapFont1 = new BitmapFont();
		bitmapFont1.setColor(Color.RED);
		bitmapFont1.getData().setScale(5);

		bitmapFont2 = new BitmapFont();
		bitmapFont2.setColor(Color.RED);
		bitmapFont2.getData().setScale(5);


		random = new Random();

		dirBottom=Gdx.files.internal("imgbottom");
		dirTop=Gdx.files.internal("imgtop");


		distanceBetweenCactus=GAME_WIDTH;

		bottomCactusRectangles = new Rectangle[numberOfCactus];
		bottomCactusRectangles1 = new Rectangle[numberOfCactus];
		topCactusRectangles = new Rectangle[numberOfCactus];
		topCactusRectangles1 = new Rectangle[numberOfCactus];
		music = Gdx.audio.newMusic(Gdx.files.internal("Jumping bat.wav"));



		startGame();





	}

	public void resize(int width,int height) {
      viewport.update(width, height);


	}

	public void startGame() {
		ballY=GAME_HEIGHT / 8+ball.getHeight();

		music.play();
		for(int i=0;i<numberOfCactus;i++) {

			randomTextureBottom[i] = new Texture(dirBottom.list()[random.nextInt(dirBottom.list().length)]);
			randomTextureTop[i] = new Texture(dirTop.list()[random.nextInt(dirTop.list().length)]);
			randomTextureBottom1[i] = new Texture(dirBottom.list()[random.nextInt(dirBottom.list().length)]);
			randomTextureTop1[i] = new Texture(dirTop.list()[random.nextInt(dirTop.list().length)]);

			cactusX[i]=GAME_WIDTH/2 + GAME_WIDTH+ i*distanceBetweenCactus;
			cactusTopX[i]=GAME_WIDTH/3-randomTextureTop[i].getWidth()/2 - GAME_WIDTH- i*distanceBetweenCactus ;

			bottomCactusRectangles[i] = new Rectangle();
			bottomCactusRectangles1[i] = new Rectangle();
			topCactusRectangles[i] = new Rectangle();
			topCactusRectangles1[i] = new Rectangle();

		}





	}






	@Override
	public void render () {


	batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(background,0,0,GAME_WIDTH,GAME_HEIGHT);





		if(gameState==1 ) {

			if(cactusX[scoringCactus] < GAME_WIDTH / 6-ball.getWidth() ) {

				score++;

				if(score%2==0) {

					score++;
					if(score%7==0) {

						cactusVelocity++;



					}


				}
				Gdx.app.log("score:" , String.valueOf(score));


				if(scoringCactus<numberOfCactus-1) {

					scoringCactus++;
				}
				else {
					scoringCactus=0;
				}


			}


			if(Gdx.input.isTouched()) {
				if(ballY <= GAME_HEIGHT - 4*ball.getHeight()) {
                    velocity=-18;
				}

			}




			for(int i=0;i<numberOfCactus;i++) {

				if(cactusX[i] < - (5*randomTextureBottom[i].getWidth())) {

					cactusX[i]+=  numberOfCactus * distanceBetweenCactus + spaceBetweenCactus;

					randomTextureBottom[i] = new Texture(dirBottom.list()[random.nextInt(dirBottom.list().length)]);
					randomTextureBottom1[i] = new Texture(dirBottom.list()[random.nextInt(dirBottom.list().length)]);

				} else {
					cactusX[i] = cactusX[i] - cactusVelocity;

				}
				if (cactusTopX[i] > (5*randomTextureTop[i].getWidth()) ) {

					cactusTopX[i] -= numberOfCactus*distanceBetweenCactus+spaceBetweenCactus;

					randomTextureTop[i] = new Texture(dirTop.list()[random.nextInt(dirTop.list().length)]);
					randomTextureTop1[i] = new Texture(dirTop.list()[random.nextInt(dirTop.list().length)]);

				} else {
					cactusTopX[i] = cactusTopX[i] + cactusVelocity;


				}

				batch.draw(randomTextureBottom1[i], cactusX[i], GAME_HEIGHT / 4 - bottomCactus.getHeight());

				batch.draw(randomTextureTop1[i], cactusTopX[i], GAME_HEIGHT  - randomTextureTop1[i].getHeight());

				batch.draw(randomTextureBottom[i], cactusX[i] +250+ (i*spaceBetweenCactus), GAME_HEIGHT  / 4 - bottomCactus.getHeight());

				batch.draw(randomTextureTop[i], cactusTopX[i] -250- (i*spaceBetweenCactus), GAME_HEIGHT  - randomTextureTop[i].getHeight());

				bottomCactusRectangles[i] = new Rectangle(cactusX[i] +250+ (i*spaceBetweenCactus), GAME_HEIGHT  / 4 - bottomCactus.getHeight(),
						randomTextureBottom[i].getWidth()-ball.getWidth(),randomTextureBottom[i].getHeight()-ball.getWidth());


				bottomCactusRectangles1[i] = new Rectangle(cactusX[i], GAME_HEIGHT  / 4 - bottomCactus.getHeight(),
						randomTextureBottom1[i].getWidth()-ball.getWidth(),randomTextureBottom1[i].getHeight()-ball.getWidth());

				topCactusRectangles[i] = new Rectangle(cactusTopX[i] -250- (i*spaceBetweenCactus), GAME_HEIGHT  - randomTextureTop[i].getHeight()
						,randomTextureTop[i].getWidth()-ball.getWidth(),randomTextureTop[i].getHeight()-ball.getWidth());

				topCactusRectangles1[i] = new Rectangle(cactusTopX[i], GAME_HEIGHT  - randomTextureTop1[i].getHeight()
						,randomTextureTop1[i].getWidth()-ball.getWidth(),randomTextureTop1[i].getHeight()-ball.getWidth());



			}





			if(ballY >GAME_HEIGHT / 8-ball.getHeight() || velocity<15 ) {
				velocity++;
				ballY = ballY - velocity-1;

			}



		}
		else if (gameState==0){
			bitmapFont1.draw(batch,"Touch to screen \n"+
					"to start the game",GAME_WIDTH*1/5, GAME_HEIGHT/2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}

		else if (gameState==2){
			batch.draw(gameOver,GAME_WIDTH/2 - gameOver.getWidth()/2, GAME_HEIGHT/2 - gameOver.getHeight()/2);
			bitmapFont.draw(batch, "Score:" + String.valueOf(score),GAME_WIDTH*1/3, GAME_HEIGHT*2/5);
			bitmapFont2.draw(batch,"Touch to screen \n"+
					"to play again",GAME_WIDTH*1/5, GAME_HEIGHT*1/5);
			music.pause();
			if (Gdx.input.justTouched()) { //it'll be called always when the screen is touched.
				gameState = 1;
				startGame();
				score=0;
				scoringCactus=0;
				velocity=0;
			}
		}







		else {
			if(Gdx.input.isTouched()) {
				gameState=1;


			}


		}




		bitmapFont.draw(batch,String.valueOf(score),GAME_WIDTH *3/4, 175);


		batch.draw(ball, GAME_WIDTH / 6+ball.getWidth()/2,ballY);
		batch.end();
		ballCircle.set(GAME_WIDTH / 6+ball.getWidth(),ballY + ball.getHeight()/2,ball.getWidth()/2);



	/*	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(ballCircle.x,ballCircle.y,ballCircle.radius);*/
		for(int i=0;i<numberOfCactus;i++) {

        /*	shapeRenderer.rect(cactusX[i] +250+ (i*spaceBetweenCactus), Gdx.graphics.getHeight() / 4 - bottomCactus.getHeight(),
					randomTextureBottom[i].getWidth(),randomTextureBottom[i].getHeight());

            shapeRenderer.rect(cactusX[i], Gdx.graphics.getHeight() / 4 - bottomCactus.getHeight(),
					randomTextureBottom1[i].getWidth(),randomTextureBottom1[i].getHeight());

            shapeRenderer.rect(cactusTopX[i] -250- (i*spaceBetweenCactus), Gdx.graphics.getHeight() - randomTextureTop[i].getHeight()
					,randomTextureTop[i].getWidth(),randomTextureTop[i].getHeight());


            shapeRenderer.rect(cactusTopX[i], Gdx.graphics.getHeight() - randomTextureTop1[i].getHeight()
					,randomTextureTop1[i].getWidth(),randomTextureTop1[i].getHeight());*/

			if(Intersector.overlaps(ballCircle,bottomCactusRectangles[i]) || Intersector.overlaps(ballCircle,bottomCactusRectangles1[i])
					|| Intersector.overlaps(ballCircle,topCactusRectangles[i])
					|| Intersector.overlaps(ballCircle,topCactusRectangles1[i]))
			{

				gameState=2;

			}




		}



		// shapeRenderer.end();


	}

}
