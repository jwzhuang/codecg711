/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.media.server.impl.dsp.audio.g729;

public class DecGain {
	float past_qua_en[]=new float[]{(float)-14.0,(float)-14.0,(float)-14.0,(float)-14.0};
	public void dec_gain(
	 int index,             /* input : quantizer index              */
	 float code[],          /* input : fixed code book vector       */
	 int l_subfr,           /* input : subframe size                */
	 int bfi,               /* input : bad frame indicator good = 0 */
	 FloatPointer gain_pit,       /* output: quantized acb gain           */
	 FloatPointer gain_code       /* output: quantized fcb gain           */
	)
	{
	   

	   int    index1,index2;
	   float  g_code;
	   FloatPointer gcode0 = new FloatPointer();

	   /*----------------- Test erasure ---------------*/
	   if (bfi != 0)
	     {
	        gain_pit.value *= (float)0.9;
	        if(gain_pit.value > (float)0.9) gain_pit.value=(float)0.9;
	        gain_code.value *= (float)0.98;

	     /*----------------------------------------------*
	      * update table of past quantized energies      *
	      *                              (frame erasure) *
	      *----------------------------------------------*/
	      GainPred.gain_update_erasure(past_qua_en);

	        return;
	     }

	   /*-------------- Decode pitch gain ---------------*/

	   index1 = TabLD8k.imap1[index/LD8KConstants.NCODE2] ;
	   index2 = TabLD8k.imap2[index%LD8KConstants.NCODE2] ;
	   gain_pit.value = TabLD8k.gbk1[index1][0]+TabLD8k.gbk2[index2][0] ;

	   /*-------------- Decode codebook gain ---------------*/

	  /*---------------------------------------------------*
	   *-  energy due to innovation                       -*
	   *-  predicted energy                               -*
	   *-  predicted codebook gain => gcode0[exp_gcode0]  -*
	   *---------------------------------------------------*/

	   GainPred.gain_predict( past_qua_en, code, l_subfr, gcode0);

	  /*-----------------------------------------------------------------*
	   * *gain_code = (gbk1[indice1][1]+gbk2[indice2][1]) * gcode0;      *
	   *-----------------------------------------------------------------*/

	   g_code = TabLD8k.gbk1[index1][1]+TabLD8k.gbk2[index2][1];
	   gain_code.value =  g_code * gcode0.value;

	  /*----------------------------------------------*
	   * update table of past quantized energies      *
	   *----------------------------------------------*/

	   GainPred.gain_update( past_qua_en, g_code);

	   return;
	}

}
