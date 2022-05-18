package application;



import java.util.ArrayList;
import java.util.Random;

public class Monde {
	private Secteur[][] lesSecteurs;
	private ArrayList<Robot> lesRobots;
	private ArrayList<Entrepot> lesEntrepots;
	private ArrayList<Mine> lesMines;
	
	public Monde() {
		this.lesSecteurs = new Secteur[10][10];
		lesRobots = new ArrayList<Robot>();
		lesEntrepots = new ArrayList<Entrepot>();
		lesMines = new ArrayList<Mine>();
	}
	
	public Monde createWorld()
	{
		Random random = new Random();
		int compEnt = 0;
		int compMine = 0;
		int compEau = 0;
		int compRob = 0;
		for(int i=0;i<10;i++) {
			for(int u =0; u<10;u++ ) {
				int ver = random.nextInt(20);
				Secteur sec = new Secteur(i,u,this);
				if(ver > 1 && ver < 4 && compEau <= 10) {
					sec = new Secteur(i,u,this);
					sec.setEau(true);
					compEau+=1;
				}
				if(ver == 6 && compEnt < 2) {
					if(compEnt>0) {
						this.lesEntrepots.add(new Entrepot(compEnt+1,"OR", sec));
						sec.setEntrepot(this.lesEntrepots.get(compEnt));
					}
					else {
						this.lesEntrepots.add(new Entrepot(compEnt+1,"NI",sec));
						sec.setEntrepot(this.lesEntrepots.get(compEnt));
					}
					compEnt+=1;
				}
				if(ver == 8 && compMine < 4) {
					this.lesMines.add(new Mine(compMine+1,"OR",0,sec));
					sec.setMine(this.lesMines.get(compMine));
					compMine+=1;
				}
				if(ver == 10 && compRob<5) {
					this.lesRobots.add(new Robot(compRob+1,"OR",0,sec,0));
					sec.setRobot(this.lesRobots.get(compRob));
					compRob+=1;
				}
				this.lesSecteurs[i][u] = sec;
			}
		}
		this.alea();
		this.setTypeManquant();
		lesMines.forEach(lesMines -> System.out.println());
		return this;
	}
	

	
	public Secteur[][] getSecteurs() {
		return this.lesSecteurs;
	}
	
	public void alea() {
		Random ran = new Random();
		if(this.lesRobots.size()==1) {
			while(this.lesRobots.size()==1) {
				int x = ran.nextInt(9); int y = ran.nextInt(9);
				if(!this.lesSecteurs[x][y].getEau() && !this.lesSecteurs[x][y].haveRobot()) {
					Robot rob = new Robot(2,"OR",0,this.lesSecteurs[x][y],0);
					this.lesRobots.add(rob);
					this.lesSecteurs[x][y].setRobot(rob);
				}
			}
		}
		if(this.lesMines.size()==1) {
			while(this.lesMines.size()==1) {
				int x = ran.nextInt(9); int y = ran.nextInt(9);
				if(!this.lesSecteurs[x][y].getEau() && !this.lesSecteurs[x][y].haveRobot() && !this.lesSecteurs[x][y].haveEntrepot() && !this.lesSecteurs[x][y].haveMine()) {
					Mine min = new Mine(2,"OR",0,this.lesSecteurs[x][y]);
					this.lesMines.add(min);
					this.lesSecteurs[x][y].setMine(min);
				}
			}
		}
		if(this.lesEntrepots.size()==1) {
			while(this.lesEntrepots.size()==1) {
				int x = ran.nextInt(9); int y = ran.nextInt(9);
				if(!this.lesSecteurs[x][y].getEau() && !this.lesSecteurs[x][y].haveRobot() && !this.lesSecteurs[x][y].haveEntrepot() && !this.lesSecteurs[x][y].haveMine()) {
					Entrepot ent = new Entrepot(2,"OR",this.lesSecteurs[x][y]);
					this.lesEntrepots.add(ent);
					this.lesSecteurs[x][y].setEntrepot(ent);
				}
			}
		}
		for(int i=0;i<this.lesMines.size();i++) {
			Mine obj = this.lesMines.get(i);
			int alea = ran.nextInt(50);
			alea+=50;
			obj.setCapaciteStockageMax(alea);
			obj.setCapacite(alea);
			obj.aleaMinerai();
		}
		for(int i=0;i<this.lesRobots.size();i++) {
			Robot obj = this.lesRobots.get(i);
			int alea = ran.nextInt(8);
			alea+=1;
			obj.setCapaciteStockageMax(alea);
			alea = ran.nextInt(2);
			alea+=1;
			obj.setCapaciteMinage(alea);
			obj.setCapacite(0);
			obj.aleaMinerai();
		}
	}
	
	public void setTypeManquant(){
		int compNickel =0;
		int compOr = 0;
		Random ran = new Random();
		for(int i =0; i<this.lesRobots.size();i++) {
			if(this.lesRobots.get(i).getTypeMinerai()=="NI") {
				compNickel +=1;
			}
			else compOr+=1;
		}
		if(compNickel<1) {
			this.lesRobots.get(ran.nextInt(this.lesRobots.size())).setTypeMinerai("NI");
		}
		else if(compOr<1) {
			this.lesRobots.get(ran.nextInt(this.lesRobots.size())).setTypeMinerai("OR");
		}
		compNickel = 0;
		compOr = 0;
		for(int i =0; i<this.lesMines.size();i++) {
			if(this.lesMines.get(i).getTypeMinerai()=="NI") {
				compNickel +=1;
			}
			else compOr+=1;
		}
		if(compNickel<1) {
			this.lesMines.get(ran.nextInt(this.lesMines.size())).setTypeMinerai("NI");
		}
		else if(compOr<1) {
			this.lesMines.get(ran.nextInt(this.lesMines.size())).setTypeMinerai("OR");
		}
	}
	public ArrayList<Robot> getRobots(){
		return this.lesRobots;
	}
	public ArrayList<Entrepot> getEntrepots(){
		return this.lesEntrepots;
	}
	public ArrayList<Mine> getMines(){
		return this.lesMines;
	}
}
