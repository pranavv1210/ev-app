import { motion } from "framer-motion";
import { ArrowRight } from "lucide-react";

export default function Waitlist() {
  return (
    <section id="waitlist" className="py-48 relative overflow-hidden bg-slate-50">
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-primary-container/20 via-background to-background"></div>
      
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10">
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          whileInView={{ opacity: 1, scale: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1] }}
          className="glass-panel p-12 md:p-32 rounded-[4rem] text-center relative overflow-hidden border border-primary-container/30 bg-surface-container-lowest/50 backdrop-blur-3xl shadow-2xl"
        >
          <div className="relative z-10 max-w-4xl mx-auto flex flex-col items-center">
            <div className="mb-10 inline-block bg-primary-container/10 border border-primary-container/30 rounded-full px-6 py-2 text-primary-container font-bold tracking-widest text-sm uppercase shadow-[0_0_20px_rgba(77,168,255,0.1)]">
              Launching First in Bengaluru, Karnataka 🇮🇳
            </div>
            
            <h2 className="font-display-lg-mobile md:font-display-lg text-[10vw] md:text-[5vw] leading-[1.1] tracking-tighter text-primary mb-8">
              Help shape the future of <span className="gradient-text italic">Bengaluru Mobility by Yaatra EV </span>
            </h2>
            
            <p className="text-xl md:text-2xl text-text-secondary mb-8 font-light">Join the early access list today.</p>
            
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.3 }}
              className="bg-background/80 backdrop-blur-md border border-secondary/30 px-6 py-8 md:px-12 rounded-3xl mb-12 shadow-2xl relative overflow-hidden"
            >
              <div className="absolute inset-0 bg-gradient-to-r from-secondary/5 via-transparent to-secondary/5"></div>
              <div className="relative z-10 flex flex-col items-center">
                <span className="text-secondary font-bold text-sm uppercase tracking-widest mb-3 inline-block px-4 py-1 rounded-full bg-secondary/10">Limited Time Offer</span>
                <p className="text-primary text-xl md:text-3xl font-light leading-snug">
                  Sign up now and receive <span className="font-bold text-secondary">₹500 in ride credits</span> + <br className="hidden md:block" />
                  <span className="font-bold">Exclusive Founder Status</span> upon launch.
                </p>
              </div>
            </motion.div>
            
            <a 
              href="https://docs.google.com/forms/d/e/1FAIpQLScU85kgbPmiIvhF3nuTBkpgXawRpTDbzRYWVcJZ0KvLFLEEcg/viewform?usp=publish-editor" 
              target="_blank" 
              rel="noopener noreferrer" 
              className="group inline-flex items-center gap-4 bg-primary-container text-on-primary font-bold px-12 py-6 rounded-full electric-glow whitespace-nowrap hover:scale-105 transition-all active:scale-95 text-xl md:text-2xl"
            >
              Join the Waitlist
              <ArrowRight className="group-hover:translate-x-2 transition-transform" />
            </a>
            
            <p className="mt-12 text-sm text-text-secondary tracking-widest uppercase">
              Be part of the exclusive group to experience premium EV mobility before the public launch.
            </p>
          </div>
        </motion.div>
      </div>
    </section>
  );
}
