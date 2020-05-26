public class Simplex {
    private int filas, 
    			columnas; 
    private float[][] tabla; // tabla simplex
    private boolean noHaySolucion = false;
    private float 	z,
    				x,
    				y;
    private resultado res;

    public static enum resultado{
        no_optimo,
        optimo,
        sinSolucion
    };
    
    public Simplex(int restricciones, int incognitas){
    	//inicializacion
        filas = restricciones+1; 
        columnas = incognitas+1;   
        tabla = new float[filas][columnas]; 
        
       
    }
    
    // imprime la tabla simplex
    public void print(){
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                String value = String.format("%.2f", tabla[i][j]);
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // llena la tabla
    public void llenaTabla(float[][] datos){
        for(int i = 0; i < tabla.length; i++){
            System.arraycopy(datos[i], 0, this.tabla[i], 0, datos[i].length);
        }
    }
    
   //calcula hasta encontrar el resultado optimo
    public resultado calcula(){
        
        if(revisaOptimo()){
        	res=resultado.optimo;
            return resultado.optimo; // el resultado es optimo
            
        }
        
       
        // encuentra la columna pivote
        int columnaPivote = encuentraColumnaPivote();

        // encuentra el valor pivote
        float[] valores = divisionColumnaPivote(columnaPivote);
        if(noHaySolucion == true) {
        	res=resultado.sinSolucion;
        	return resultado.sinSolucion;
        }
            
        int filaPivote = encuentraValorMenor(valores);

        // actualiza la tabla
        creaSiguienteTabla(filaPivote, columnaPivote);
        return resultado.no_optimo;
    }
    
    // forma una tabla nueva con los calculos hechos
    private void creaSiguienteTabla(int filaPivote, int columnaPivote){
        float valorPivote = tabla[filaPivote][columnaPivote];
        float[] valoresFilaPivote = new float[columnas];
        float[] valoresColumnaPivote = new float[columnas];
        float[] nuevaFila = new float[columnas];
        
        // divide todas las entradas en filaPivote por la entrada en columnaPivote 
        // obtiene los valores de filaPivote
        System.arraycopy(tabla[filaPivote], 0, valoresFilaPivote, 0, columnas);
        
        // obtiene los valores de columnaPivote
        for(int i = 0; i < filas; i++)
            valoresColumnaPivote[i] = tabla[i][columnaPivote];
        
        // divide valores en filaPivote por el valor pivote
        for(int  i = 0; i < columnas; i++)
            nuevaFila[i] =  valoresFilaPivote[i] / valorPivote;
        
        
        for(int i = 0; i < filas; i++){
            if(i != filaPivote){
                for(int j = 0; j < columnas; j++){
                    float c = valoresColumnaPivote[i];
                    tabla[i][j] = tabla[i][j] - (c * nuevaFila[j]);
                }
            }
        }
        
        // replace the row
        System.arraycopy(nuevaFila, 0, tabla[filaPivote], 0, nuevaFila.length);
    }
    
    // hace las divisiones de la columna pivote
    private float[] divisionColumnaPivote(int column){
        float[] entradasPositivas = new float[filas];
        float[] res = new float[filas];
        int negativos = 0; 
        for(int i = 0; i < filas; i++){
            if(tabla[i][column] > 0){
                entradasPositivas[i] = tabla[i][column];
            }
            else{
                entradasPositivas[i] = 0;
                negativos++;
            }
             
        }
        
        if(negativos == filas)
            this.noHaySolucion = true;
        else{
            for(int i = 0;  i < filas; i++){
                float val = entradasPositivas[i];
                if(val > 0){
                    res[i] = tabla[i][columnas -1] / val;
                }
            }
        }
        
        return res;
    }
    
    // finds the next entering column
    private int encuentraColumnaPivote(){
        float[] values = new float[columnas];
        int posicion = 0;
        int count=0;
        
        for(int i = 0; i < columnas-1; i++){
            if(tabla[filas-1][i] < 0){
                count++;
            }
        }
        
        
        if(count > 1){
            for(int i = 0; i < columnas-1; i++)
                values[i] = Math.abs(tabla[filas-1][i]);
            posicion = encuentraMayor(values);
        } else posicion = count - 1;
        
        return posicion;
    }
    
    
    // encunetra el valor menor en el arreglo 
    private int encuentraValorMenor(float[] data){
        float menor= data[0];
        int  posicion = 0;
         
        
        for(int i = 1; i < data.length; i++){
            if(data[i] > 0){
                if(Float.compare(data[i], menor) < 0){
                    menor = data[i];
                    posicion  =i;
                }
            }
        }
        
        return posicion;
    }
    
    // encuentra el valor mayor
    private int encuentraMayor(float[] data){
        float mayor;
        int  posicion = 0;
        mayor = data[0];
        
        for(int i = 1; i < data.length; i++){
            if(Float.compare(data[i], mayor) > 0){
                mayor = data[i];
                posicion  = i;
            }
        }
        
        return posicion;
    }
    
    // revisa si es optimo
    public boolean revisaOptimo(){
        boolean optima = false;
        int vCount = 0;
        
        for(int i = 0; i < columnas-1; i++){
            float val = tabla[filas-1][i];
            if(val >= 0){
                vCount++;
            }
        }
        
        if(vCount == columnas-1){
            optima = true;
        }
        
        return optima;
    }

    // returns the simplex tableau
    public float[][] getTable() {
        return tabla;
    }
    public void setZ() {
    	this.z=this.tabla[filas-1][columnas-1];
    	
    }
    public float getZ() {
    	return this.z;
    }
    public void setX() {
    	for(int i=0;i<this.filas;i++) {
    		if(this.tabla[i][0]==1) {
    			this.x=this.tabla[i][columnas-1];
    		}
    	}
    	
    	
    }
    public float getX() {
    	return this.x;
    }
    public void setY() {
    	for(int i=0;i<this.filas;i++) {
    		if(this.tabla[i][1]==1) {
    			this.y=this.tabla[i][columnas-1];
    			break;
    		}
    	}
    	
    	
    	
    }
    public float getY() {
    	return this.y;
    }
    public resultado getRes() {
    	return this.res;
    }
    public void iteracion() {
    	boolean fin=false;
    	 while(!fin){
             this.calcula();

             if( this.getRes()== Simplex.resultado.optimo){
                 this.print();
                 this.setZ();
                 this.setX();
                 this.setY();
                 System.out.println("Z= "+this.getZ());
                 System.out.println("X= "+this.getX());
                 System.out.println("Y= "+this.getY());
                 fin = true;
             }
             else if(this.getRes() == Simplex.resultado.sinSolucion){
                 System.out.println("sin solucion");
                 fin = true;
             }
         }
    }
}