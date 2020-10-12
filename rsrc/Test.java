public class example {
    public void CheckIfs(int a, int b) {
        int result = a + b;
        if(result>0 || result == 0) {
            String colour = "red";
        }
        else if(result<0){
            String colour = "blue";
        }

        if(i<4){

        }
        else{
            return;
        }
    }

    public int CheckLoops(int x, int y){
        while(x == 0 && y >50){
            x ++;
            while(y >50) {
                y--;
            }
        }
        for(int i = 0; i++; i<5){

        }
        for (int j: list) {

        }
        do{

        }while (x<5);
        return x;
    }

    public int CheckSwitch(int x, int y){
        int r = x;
        switch (r) {
            case x:
                r -= x;
                return r;
            case y:
                r += y;
                return r;
            default:
                break;
        }
        return r;
    }
}