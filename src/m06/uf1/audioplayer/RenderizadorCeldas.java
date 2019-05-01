/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Cho_S
 */
class RenderizadorCeldas extends DefaultTableCellRenderer{
    
    private String tipo="text";
    private Font courier = new Font( "Courier New",Font.PLAIN ,12 );
    private Font normal = new Font( "Arial",Font.PLAIN ,12 );
    private Font bold = new Font( "Arial",Font.BOLD ,12 );
    
    @Override
    public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column )
    {
        this.setText((String)value);
        this.setForeground(new Color(0,0,0));
        if (selected) {
            this.setBackground( new Color( 50, 153 , 254) );
            this.setFont(bold);
        } else {
            this.setFont(normal);
            if(row%2== 0){
                this.setBackground(Color.white);
            }else{
                this.setBackground(new Color(150,200,255));
            }
        }
        if(column == 1){
            this.setForeground(Color.red);
            this.setFont(courier);
        }
        
        return this;
    }
}
