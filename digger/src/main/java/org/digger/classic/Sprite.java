class Sprite {

Digger dig;
	
boolean retrflag=true;

boolean sprrdrwf[]={false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};	// [17]
boolean sprrecf[]={false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};	// [17]
boolean sprenf[]={false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,};	// [16]

int sprch[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [17]

short sprmov[][]={null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};	// [16]

int sprx[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [17]
int spry[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [17]
int sprwid[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [17]
int sprhei[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [17]
int sprbwid[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprbhei[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprnch[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprnwid[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprnhei[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprnbwid[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]
int sprnbhei[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	// [16]

int defsprorder[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};	// [16]
int sprorder[] = defsprorder;

Sprite (Digger d) {
	dig = d;
}
boolean bcollide (int bx,int si) {
  if (sprx[bx]>=sprx[si]) {
	if (sprx[bx]+sprbwid[bx]>sprwid[si]*4+sprx[si]-sprbwid[si]-1)
	  return false;
  }
  else
	if (sprx[si]+sprbwid[si]>sprwid[bx]*4+sprx[bx]-sprbwid[bx]-1)
	  return false;
  if (spry[bx]>=spry[si]) {
	if (spry[bx]+sprbhei[bx]<=sprhei[si]+spry[si]-sprbhei[si]-1)
	  return true;
	return false;
  }
  if (spry[si]+sprbhei[si]<=sprhei[bx]+spry[bx]-sprbhei[bx]-1)
	return true;
  return false;
}
int bcollides (int bx) {
  int si=bx,ax=0,dx=0;
  bx=0;
  do {
	if (sprenf[bx] && bx!=si) {
	  if (bcollide(bx,si))
		ax|=1<<dx;
	  sprx[bx]+=320;
	  spry[bx]-=2;
	  if (bcollide(bx,si))
		ax|=1<<dx;
	  sprx[bx]-=640;
	  spry[bx]+=4;
	  if (bcollide(bx,si))
		ax|=1<<dx;
	  sprx[bx]+=320;
	  spry[bx]-=2;
	}
	bx++;
	dx++;
  } while (dx!=16);
  return ax;
}
void clearrdrwf () {
  int i;
  clearrecf();
  for (i=0;i<17;i++)
	sprrdrwf[i]=false;
}
void clearrecf () {
  int i;
  for (i=0;i<17;i++)
	sprrecf[i]=false;
}
boolean collide (int bx,int si) {
  if (sprx[bx]>=sprx[si]) {
	if (sprx[bx]>sprwid[si]*4+sprx[si]-1)
	  return false;
  }
  else
	if (sprx[si]>sprwid[bx]*4+sprx[bx]-1)
	  return false;
  if (spry[bx]>=spry[si]) {
	if (spry[bx]<=sprhei[si]+spry[si]-1)
	  return true;
	return false;
  }
  if (spry[si]<=sprhei[bx]+spry[bx]-1)
	return true;
  return false;
}
void createspr (int n,int ch,short[] mov,int wid,int hei,int bwid,int bhei) {
  sprnch[n&15]=sprch[n&15]=ch;
  sprmov[n&15]=mov;
  sprnwid[n&15]=sprwid[n&15]=wid;
  sprnhei[n&15]=sprhei[n&15]=hei;
  sprnbwid[n&15]=sprbwid[n&15]=bwid;
  sprnbhei[n&15]=sprbhei[n&15]=bhei;
  sprenf[n&15]=false;
}
void drawmiscspr (int x,int y,int ch,int wid,int hei) {
  sprx[16]=x&-4;
  spry[16]=y;
  sprch[16]=ch;
  sprwid[16]=wid;
  sprhei[16]=hei;
  dig.Pc.gputim(sprx[16],spry[16],sprch[16],sprwid[16],sprhei[16]);
}
int drawspr (int n,int x,int y) {
  int bx,t1,t2,t3,t4;
  bx=n&15;
  x&=-4;
  clearrdrwf();
  setrdrwflgs(bx);
  t1=sprx[bx];
  t2=spry[bx];
  t3=sprwid[bx];
  t4=sprhei[bx];
  sprx[bx]=x;
  spry[bx]=y;
  sprwid[bx]=sprnwid[bx];
  sprhei[bx]=sprnhei[bx];
  clearrecf();
  setrdrwflgs(bx);
  sprhei[bx]=t4;
  sprwid[bx]=t3;
  spry[bx]=t2;
  sprx[bx]=t1;
  sprrdrwf[bx]=true;
  putis();
  sprx[bx]=x;
  spry[bx]=y;
  sprch[bx]=sprnch[bx];
  sprwid[bx]=sprnwid[bx];
  sprhei[bx]=sprnhei[bx];
  sprbwid[bx]=sprnbwid[bx];
  sprbhei[bx]=sprnbhei[bx];
  dig.Pc.ggeti(sprx[bx],spry[bx],sprmov[bx],sprwid[bx],sprhei[bx]);
  putims();
  return bcollides(bx);
}
void erasespr (int n) {
  int bx=n&15;
  dig.Pc.gputi(sprx[bx],spry[bx],sprmov[bx],sprwid[bx],sprhei[bx],true);
  sprenf[bx]=false;
  clearrdrwf();
  setrdrwflgs(bx);
  putims();
}
void getis () {
  int i;
  for (i=0;i<16;i++)
	if (sprrdrwf[i])
	  dig.Pc.ggeti(sprx[i],spry[i],sprmov[i],sprwid[i],sprhei[i]);
  putims();
}
void initmiscspr (int x,int y,int wid,int hei) {
  sprx[16]=x;
  spry[16]=y;
  sprwid[16]=wid;
  sprhei[16]=hei;
  clearrdrwf();
  setrdrwflgs(16);
  putis();
}
void initspr (int n,int ch,int wid,int hei,int bwid,int bhei) {
  sprnch[n&15]=ch;
  sprnwid[n&15]=wid;
  sprnhei[n&15]=hei;
  sprnbwid[n&15]=bwid;
  sprnbhei[n&15]=bhei;
}
int movedrawspr (int n,int x,int y) {
  int bx=n&15;
  sprx[bx]=x&-4;
  spry[bx]=y;
  sprch[bx]=sprnch[bx];
  sprwid[bx]=sprnwid[bx];
  sprhei[bx]=sprnhei[bx];
  sprbwid[bx]=sprnbwid[bx];
  sprbhei[bx]=sprnbhei[bx];
  clearrdrwf();
  setrdrwflgs(bx);
  putis();
  dig.Pc.ggeti(sprx[bx],spry[bx],sprmov[bx],sprwid[bx],sprhei[bx]);
  sprenf[bx]=true;
  sprrdrwf[bx]=true;
  putims();
  return bcollides(bx);
}
void putims () {
  int i,j;
  for (i=0;i<16;i++) {
	j=sprorder[i];
	if (sprrdrwf[j])
	  dig.Pc.gputim(sprx[j],spry[j],sprch[j],sprwid[j],sprhei[j]);
  }
}
void putis () {
  int i;
  for (i=0;i<16;i++)
	if (sprrdrwf[i])
	  dig.Pc.gputi(sprx[i],spry[i],sprmov[i],sprwid[i],sprhei[i]);
}
void setrdrwflgs (int n) {
  int i;
  if (!sprrecf[n]) {
	sprrecf[n]=true;
	for (i=0;i<16;i++)
	  if (sprenf[i] && i!=n) {
		if (collide(i,n)) {
		  sprrdrwf[i]=true;
		  setrdrwflgs(i);
		}
		sprx[i]+=320;
		spry[i]-=2;
		if (collide(i,n)) {
		  sprrdrwf[i]=true;
		  setrdrwflgs(i);
		}
		sprx[i]-=640;
		spry[i]+=4;
		if (collide(i,n)) {
		  sprrdrwf[i]=true;
		  setrdrwflgs(i);
		}
		sprx[i]+=320;
		spry[i]-=2;
	  }
  }
}
void setretr (boolean f) {
  retrflag=f;
}
void setsprorder (int[] newsprorder) {
  if (newsprorder==null)
		sprorder=defsprorder;
  else
		sprorder=newsprorder;
}
}