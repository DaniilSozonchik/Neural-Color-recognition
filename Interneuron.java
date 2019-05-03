import java.util.ArrayList;

/**
 * The class Neuron implents a interneuron for the class Network.
 * 
 * @author Daniil Sozonchik
 * @version 1.0
 * @since 2019-04-29
 */
public class Interneuron extends Neuron {
	/**
	 * {@inheritDoc}
	 */
	public Interneuron(int index) {
		super(index);
		outgoingsynapses = new ArrayList<Synapse>();
	}

	/**
	 * Divides incoming signal into equal parts for all the outgoing synapses
	 * 
	 * @param input 3 dimensional signal from another neuron
	 * @return 3 dimensional neural signal (after processing)
	 */
	@Override
	public double[] integrateSignal(double[] signal) {
		for (int i = 0; i < signal.length; i++){
			signal[i] = signal[i] / this.outgoingsynapses.size();
		}
		for (Synapse syn: outgoingsynapses){
			syn.transmit(signal);
		}
		return signal;
	}
}

