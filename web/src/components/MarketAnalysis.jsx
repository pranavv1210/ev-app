import { motion } from "framer-motion";
import { TrendingUp, BatteryCharging, Leaf } from "lucide-react";

export default function MarketAnalysis() {
  return (
    <section id="market-analysis" className="py-32 md:py-48 relative overflow-hidden bg-surface-container-lowest border-y border-glass-border/20">
      <div className="absolute top-0 right-0 w-1/2 h-1/2 bg-[radial-gradient(ellipse_at_top_right,_var(--tw-gradient-stops))] from-primary-container/5 via-transparent to-transparent"></div>
      
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10">
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="mb-20 md:mb-32 max-w-4xl"
        >
          <span className="text-secondary font-bold tracking-widest text-sm uppercase mb-4 inline-block">The Economics</span>
          <h2 className="font-headline-xl text-4xl md:text-6xl text-primary leading-tight mb-8">
            Why Bengaluru's transition is <span className="italic text-secondary">inevitable.</span>
          </h2>
          <p className="text-xl md:text-2xl text-text-secondary font-light leading-relaxed">
            The data is clear. Bengaluru will see a dominant rise in EV autos over the next hardware lifecycle. 
            Here is the breakdown of why the shift is accelerating rapidly.
          </p>
        </motion.div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 md:gap-12">
          
          <motion.div 
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.1 }}
            className="glass-panel p-8 md:p-12 rounded-[2rem] border border-glass-border/30 bg-background hover:-translate-y-2 transition-transform duration-500"
          >
            <div className="w-14 h-14 rounded-2xl bg-secondary/10 flex items-center justify-center mb-8">
              <TrendingUp className="text-secondary" size={28} />
            </div>
            <h3 className="text-primary font-bold text-2xl mb-4">Overwhelming Savings</h3>
            <p className="text-text-secondary leading-relaxed">
              With <strong className="text-primary">₹1.5+ per km operational savings</strong>, the economic incentive for drivers is impossible to ignore. This translates to a massive increase in take-home pay, making the transition financially inevitable within 3 to 5 years.
            </p>
          </motion.div>

          <motion.div 
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.2 }}
            className="glass-panel p-8 md:p-12 rounded-[2rem] border border-glass-border/30 bg-background hover:-translate-y-2 transition-transform duration-500"
          >
            <div className="w-14 h-14 rounded-2xl bg-error/10 flex items-center justify-center mb-8">
              <BatteryCharging className="text-error" size={28} />
            </div>
            <h3 className="text-primary font-bold text-2xl mb-4">Infrastructure Reality</h3>
            <p className="text-text-secondary leading-relaxed">
              While charging infrastructure will create localized bottlenecks in the short term, fleet operators are actively bypassing these constraints using distributed battery-swapping networks and dedicated hubs.
            </p>
          </motion.div>

          <motion.div 
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.3 }}
            className="glass-panel p-8 md:p-12 rounded-[2rem] border border-glass-border/30 bg-background hover:-translate-y-2 transition-transform duration-500"
          >
            <div className="w-14 h-14 rounded-2xl bg-primary-container/20 flex items-center justify-center mb-8">
              <Leaf className="text-primary-container" size={28} />
            </div>
            <h3 className="text-primary font-bold text-2xl mb-4">The Namma Auto Project</h3>
            <p className="text-text-secondary leading-relaxed">
              Social enterprises like the <strong className="text-primary">EU-funded Namma Auto Project</strong> are actively financing the transition for independent drivers, transforming the auto-rickshaw sector into a highly structured, eco-friendly transit network.
            </p>
          </motion.div>

        </div>
      </div>
    </section>
  );
}
