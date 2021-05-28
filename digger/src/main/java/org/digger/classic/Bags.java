package org.digger.classic;

class Bags {

Digger dig;
	
_bag[] bagdat1 = { new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag () }, bagdat2 = { new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag () }, bagdat  = { new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag (), new _bag () };

int pushcount=0,goldtime=0;

int wblanim[]={2,0,1,0};	// [4]

Bags (Digger d) {
	dig = d;
}
int bagbits () {
  int bag,b,bags=0;
  for (bag=1,b=2;bag<8;bag++,b<<=1)
	if (bagdat[bag].exist)
	  bags|=b;
  return bags;
}
void baghitground (int bag) {
  int bn,b,clbits;
  if (bagdat[bag].dir==6 && bagdat[bag].fallh>1)
	bagdat[bag].gt=1;
  else
	bagdat[bag].fallh=0;
  bagdat[bag].dir=-1;
  bagdat[bag].wt=15;
  bagdat[bag].wobbling=false;
  clbits=dig.Drawing.drawgold(bag,0,bagdat[bag].x,bagdat[bag].y);
  dig.Main.incpenalty();
  for (bn=1,b=2;bn<8;bn++,b<<=1)
	if ((b&clbits)!=0)
	  removebag(bn);
}
int bagy (int bag) {
  return bagdat[bag].y;
}
void cleanupbags () {
  int bpa;
  dig.Sound.soundfalloff();
  for (bpa=1;bpa<8;bpa++) {
	if (bagdat[bpa].exist && ((bagdat[bpa].h==7 && bagdat[bpa].v==9) ||
		bagdat[bpa].xr!=0 || bagdat[bpa].yr!=0 || bagdat[bpa].gt!=0 ||
		bagdat[bpa].fallh!=0 || bagdat[bpa].wobbling)) {
	  bagdat[bpa].exist=false;
	  dig.Sprite.erasespr(bpa);
	}
  if (dig.Main.getcplayer()==0)
  	bagdat1[bpa].copyFrom (bagdat[bpa]);
	else
		bagdat2[bpa].copyFrom (bagdat[bpa]);
  }
}
void dobags () {
  int bag;
  boolean soundfalloffflag=true,soundwobbleoffflag=true;
  for (bag=1;bag<8;bag++)
	if (bagdat[bag].exist) {
	  if (bagdat[bag].gt!=0) {
		if (bagdat[bag].gt==1) {
		  dig.Sound.soundbreak();
		  dig.Drawing.drawgold(bag,4,bagdat[bag].x,bagdat[bag].y);
		  dig.Main.incpenalty();
		}
		if (bagdat[bag].gt==3) {
		  dig.Drawing.drawgold(bag,5,bagdat[bag].x,bagdat[bag].y);
		  dig.Main.incpenalty();
		}
		if (bagdat[bag].gt==5) {
		  dig.Drawing.drawgold(bag,6,bagdat[bag].x,bagdat[bag].y);
		  dig.Main.incpenalty();
		}
		bagdat[bag].gt++;
		if (bagdat[bag].gt==goldtime)
		  removebag(bag);
		else
		  if (bagdat[bag].v<9 && bagdat[bag].gt<goldtime-10)
			if ((dig.Monster.getfield(bagdat[bag].h,bagdat[bag].v+1)&0x2000)==0)
			  bagdat[bag].gt=goldtime-10;
	  }
	  else
		updatebag(bag);
	}
  for (bag=1;bag<8;bag++) {
	if (bagdat[bag].dir==6 && bagdat[bag].exist)
	  soundfalloffflag=false;
	if (bagdat[bag].dir!=6 && bagdat[bag].wobbling && bagdat[bag].exist)
	  soundwobbleoffflag=false;
  }
  if (soundfalloffflag)
		dig.Sound.soundfalloff();
  if (soundwobbleoffflag)
		dig.Sound.soundwobbleoff();
}
void drawbags () {
  int bag;
  for (bag=1;bag<8;bag++) {
  if (dig.Main.getcplayer()==0)
  	bagdat[bag].copyFrom (bagdat1[bag]);
	else
		bagdat[bag].copyFrom (bagdat2[bag]);
	if (bagdat[bag].exist)
	  dig.Sprite.movedrawspr(bag,bagdat[bag].x,bagdat[bag].y);
  }
}
int getbagdir (int bag) {
  if (bagdat[bag].exist)
	return bagdat[bag].dir;
  return -1;
}
void getgold (int bag) {
  int clbits;
  clbits=dig.Drawing.drawgold(bag,6,bagdat[bag].x,bagdat[bag].y);
  dig.Main.incpenalty();
  if ((clbits&1)!=0) {
	dig.Scores.scoregold();
	dig.Sound.soundgold();
	dig.digtime=0;
  }
  else
	dig.Monster.mongold();
  removebag(bag);
}
int getnmovingbags () {
  int bag,n=0;
  for (bag=1;bag<8;bag++)
	if (bagdat[bag].exist && bagdat[bag].gt<10 &&
		(bagdat[bag].gt!=0 || bagdat[bag].wobbling))
	  n++;
  return n;
}
void initbags () {
  int bag,x,y;
  pushcount=0;
  goldtime=150-dig.Main.levof10()*10;
  for (bag=1;bag<8;bag++)
	bagdat[bag].exist=false;
  bag=1;
  for (x=0;x<15;x++)
	for (y=0;y<10;y++)
	  if (dig.Main.getlevch(x,y,dig.Main.levplan())=='B')
		if (bag<8) {
		  bagdat[bag].exist=true;
		  bagdat[bag].gt=0;
		  bagdat[bag].fallh=0;
		  bagdat[bag].dir=-1;
		  bagdat[bag].wobbling=false;
		  bagdat[bag].wt=15;
		  bagdat[bag].unfallen=true;
		  bagdat[bag].x=x*20+12;
		  bagdat[bag].y=y*18+18;
		  bagdat[bag].h=x;
		  bagdat[bag].v=y;
		  bagdat[bag].xr=0;
		  bagdat[bag++].yr=0;
		}
  if (dig.Main.getcplayer()==0)
  	for (int i=1;i<8;i++)
  		bagdat1[i].copyFrom (bagdat[i]);
  else
  	for (int i=1;i<8;i++)
  		bagdat2[i].copyFrom (bagdat[i]);
}
boolean pushbag (int bag,int dir) {
  int x,y,h,v,ox,oy,clbits;
  boolean push=true;
  ox=x=bagdat[bag].x;
  oy=y=bagdat[bag].y;
  h=bagdat[bag].h;
  v=bagdat[bag].v;
  if (bagdat[bag].gt!=0) {
		getgold(bag);
		return true;
  }
  if (bagdat[bag].dir==6 && (dir==4 || dir==0)) {
		clbits=dig.Drawing.drawgold(bag,3,x,y);
		dig.Main.incpenalty();
		if (((clbits&1)!=0) && (dig.diggery>=y))
		  dig.killdigger(1,bag);
		if ((clbits&0x3f00)!=0)
		  dig.Monster.squashmonsters(bag,clbits);
		return true;
  }
  if ((x==292 && dir==0) || (x==12 && dir==4) || (y==180 && dir==6) ||
	  (y==18 && dir==2))
	push=false;
  if (push) {
	switch (dir) {
	  case 0:
		x+=4;
		break;
	  case 4:
		x-=4;
		break;
	  case 6:
		if (bagdat[bag].unfallen) {
		  bagdat[bag].unfallen=false;
		  dig.Drawing.drawsquareblob(x,y);
		  dig.Drawing.drawtopblob(x,y+21);
		}
		else
		  dig.Drawing.drawfurryblob(x,y);
		dig.Drawing.eatfield(x,y,dir);
		dig.killemerald(h,v);
		y+=6;
	}
	switch(dir) {
	  case 6:
		clbits=dig.Drawing.drawgold(bag,3,x,y);
		dig.Main.incpenalty();
		if (((clbits&1)!=0) && dig.diggery>=y)
		  dig.killdigger(1,bag);
		if ((clbits&0x3f00)!=0)
		  dig.Monster.squashmonsters(bag,clbits);
		break;
	  case 0:
	  case 4:
		bagdat[bag].wt=15;
		bagdat[bag].wobbling=false;
		clbits=dig.Drawing.drawgold(bag,0,x,y);
		dig.Main.incpenalty();
		pushcount=1;
		if ((clbits&0xfe)!=0)
		  if (!pushbags(dir,clbits)) {
			x=ox;
			y=oy;
			dig.Drawing.drawgold(bag,0,ox,oy);
			dig.Main.incpenalty();
			push=false;
		  }
		if (((clbits&1)!=0) || ((clbits&0x3f00)!=0)) {
		  x=ox;
		  y=oy;
		  dig.Drawing.drawgold(bag,0,ox,oy);
		  dig.Main.incpenalty();
		  push=false;
		}
	}
	if (push)
	  bagdat[bag].dir=dir;
	else
	  bagdat[bag].dir=dig.reversedir(dir);
	bagdat[bag].x=x;
	bagdat[bag].y=y;
	bagdat[bag].h=(x-12)/20;
	bagdat[bag].v=(y-18)/18;
	bagdat[bag].xr=(x-12)%20;
	bagdat[bag].yr=(y-18)%18;
  }
  return push;
}
boolean pushbags (int dir,int bits) {
  int bag,bit;
  boolean push=true;
  for (bag=1,bit=2;bag<8;bag++,bit<<=1)
	if ((bits&bit)!=0)
	  if (!pushbag(bag,dir))
		push=false;
  return push;
}
boolean pushudbags (int bits) {
  int bag,b;
  boolean push=true;
  for (bag=1,b=2;bag<8;bag++,b<<=1)
	if ((bits&b)!=0)
	  if (bagdat[bag].gt!=0)
		getgold(bag);
	  else
		push=false;
  return push;
}
void removebag (int bag) {
  if (bagdat[bag].exist) {
	bagdat[bag].exist=false;
	dig.Sprite.erasespr(bag);
  }
}
void removebags (int bits) {
  int bag,b;
  for (bag=1,b=2;bag<8;bag++,b<<=1)
	if ((bagdat[bag].exist) && ((bits&b)!=0))
	  removebag(bag);
}
void updatebag (int bag) {
  int x,h,xr,y,v,yr,wbl;
  x=bagdat[bag].x;
  h=bagdat[bag].h;
  xr=bagdat[bag].xr;
  y=bagdat[bag].y;
  v=bagdat[bag].v;
  yr=bagdat[bag].yr;
  switch (bagdat[bag].dir) {
	case -1:
	  if (y<180 && xr==0) {
		if (bagdat[bag].wobbling) {
		  if (bagdat[bag].wt==0) {
			bagdat[bag].dir=6;
			dig.Sound.soundfall();
			break;
		  }
		  bagdat[bag].wt--;
		  wbl=bagdat[bag].wt%8;
		  if (!((wbl&1)!=0)) {
			dig.Drawing.drawgold(bag,wblanim[wbl>>1],x,y);
			dig.Main.incpenalty();
			dig.Sound.soundwobble();
		  }
		}
		else
		  if ((dig.Monster.getfield(h,v+1)&0xfdf)!=0xfdf)
			if (!dig.checkdiggerunderbag(h,v+1))
			  bagdat[bag].wobbling=true;
	  }
	  else {
		bagdat[bag].wt=15;
		bagdat[bag].wobbling=false;
	  }
	  break;
	case 0:
	case 4:
	  if (xr==0)
		if (y<180 && (dig.Monster.getfield(h,v+1)&0xfdf)!=0xfdf) {
		  bagdat[bag].dir=6;
		  bagdat[bag].wt=0;
		  dig.Sound.soundfall();
		}
		else
		  baghitground(bag);
	  break;
	case 6:
	  if (yr==0)
		bagdat[bag].fallh++;
	  if (y>=180)
		baghitground(bag);
	  else
		if ((dig.Monster.getfield(h,v+1)&0xfdf)==0xfdf)
		  if (yr==0)
			baghitground(bag);
	  dig.Monster.checkmonscared(bagdat[bag].h);
  }
  if (bagdat[bag].dir!=-1)
	if (bagdat[bag].dir!=6 && pushcount!=0)
	  pushcount--;
	else
	  pushbag(bag,bagdat[bag].dir);
}
}